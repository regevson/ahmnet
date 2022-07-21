package at.ahmacademy.ahmnet.services.training;

import static at.ahmacademy.ahmnet.repositories.TrainingSpecification.exclId;
import static at.ahmacademy.ahmnet.repositories.TrainingSpecification.hasFreeStatus;
import static at.ahmacademy.ahmnet.repositories.TrainingSpecification.hasWeekNum;
import static at.ahmacademy.ahmnet.services.AuthService.authWhen;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.ahmacademy.ahmnet.model.SmsRequest;
import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.repositories.TrainingRepository;
import at.ahmacademy.ahmnet.repositories.TrainingSpecification;
import at.ahmacademy.ahmnet.services.sms.SmsService;
import at.ahmacademy.ahmnet.services.user.UserService;

@Service
@Scope("application")
public class TrainingService {

  private UserService userService;
  private TrainingRepository trainingRepo;
  private SmsService smsService;
  private boolean enableNotify = false;


  @PostAuthorize("hasAuthority('ADMIN') || "
               + "this.hasTrainer(returnObject, authentication.name) || "
               + "(this.isFree(returnObject) && hasAuthority('TRAINER'))")
  public Training loadTrainingById(Long trainingId) {
    return trainingRepo.findById(trainingId).orElse(null);
  }

  private Training saveTraining(Training training) {
    return trainingRepo.save(training);
  }

  @PreAuthorize("hasAuthority('ADMIN') || "
              + "this.hasTrainer(#training, authentication.name)")
  public void updateTraining(Training training) {
    saveTraining(training);
  }

  @PreAuthorize("hasAuthority('ADMIN') || "
              + "this.hasTrainer(#training, authentication.name)")
  public void deleteTraining(Training training) {
    training.getTrainingGroup().getTrainings().removeIf(t -> t.getId() == training.getId());
    trainingRepo.delete(training);
  }

  private List<List<Training>> groupByDay(List<Training> trainings) {
    List<List<Training>> trainingsByDay = new ArrayList<>();
    for(int i = 0; i<7; i++) // init list
      trainingsByDay.add(new ArrayList<>());

    for(Training training: trainings) {
      int day = training.getDateTime().getDayOfWeek().getValue() - 1;
      trainingsByDay.get(day).add(training);
    }

    return trainingsByDay;
  }

  public List<String> getDatesInWeek(int weekNum) {
    LocalDate monday = LocalDate.of(Year.now().getValue(), 2, 1)
      .with(WeekFields.of(Locale.GERMANY).getFirstDayOfWeek())
      .with(WeekFields.of(Locale.GERMANY).weekOfWeekBasedYear(), weekNum);
    return IntStream.range(0, 7).mapToObj(monday::plusDays).map(d -> d.toString())
                                                           .collect(Collectors.toList());
  }

  @PreAuthorize("hasAuthority('ADMIN') || "
              + "(hasAuthority('TRAINER') && @userService.isAuthUsr(#training.trainer.id))")
  public List<Training> saveNewTraining(Training training) {
    if(training.getLastDate() == null)
      return List.of(saveTraining(training));
    else
      return saveRecurringTraining(training);
  }

  @Transactional
  private List<Training> saveRecurringTraining(Training training) {
    LocalDate startDate = training.getDateTime().toLocalDate();
    LocalTime startTime = training.getDateTime().toLocalTime();
    LocalDate lastDate = training.getLastDate();

    List<Training> recurringTrainings = new ArrayList<>();
    while(startDate.isBefore(lastDate) || startDate.isEqual(lastDate)) {
      Training tmp = new Training(training);
      tmp.setDateTime(startDate.atTime(startTime));
      saveTraining(tmp);
      recurringTrainings.add(tmp);
      startDate = startDate.plusWeeks(1);
    }

    return recurringTrainings;
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
  public List<List<Training>> loadFreeTrainings(Integer weekNum, Optional<String> exclId) {
    Specification<Training> spec = Specification.where(hasWeekNum(weekNum))
                                            .and(hasFreeStatus(true))
                                            .and(exclId.isPresent() ? exclId(exclId.get()) : null);
    List<Training> trainings = trainingRepo.findAll(spec);
    return groupByDay(trainings);
  }

  @PreAuthorize("hasAuthority('ADMIN') || "
              + "(hasAuthority('TRAINER') && @userService.isAuthUsr(#trainerId))")
  public List<List<Training>> loadTrainingsByTrainer(String trainerId, Integer weekNum,
                                                                       Optional<Boolean> isFree) {
    Specification<Training> spec = Specification.where(hasWeekNum(weekNum))
                                          .and(TrainingSpecification.hasTrainer(trainerId))
                                          .and(isFree.isPresent() ? hasFreeStatus(isFree.get()) : null);
    List<Training> trainings = trainingRepo.findAll(spec);
    return groupByDay(trainings);
  }

  @Transactional
  public void freeTrainings(List<Training> trainings, boolean notify) {
     authWhen(userService.isAdmin() ||
              trainings.stream().allMatch(t -> hasTrainer(t, userService.getAuthUser().getId())));

    for(Training t: trainings)
      freeTraining(t);
    if(notify && this.enableNotify)
      informOfFreeing(trainings);
  }

  private void freeTraining(Training training) {
    training.setIsFree(true);
    training.setTrainer(training.getPrevTrainer());
    saveTraining(training);
  }

  @Transactional
  public void grabTrainings(List<Training> trainings, boolean notify) {
     authWhen(userService.isAdmin() ||
              trainings.stream().allMatch(t -> userService.isTrainer() && isFree(t)));

    Map<User, List<Training>> prevTrainer_trainings = new HashMap<>();
    for(Training training: trainings) {
      grabTraining(training);

      if(notify) {
        List<Training> trainingList = prevTrainer_trainings.getOrDefault(training.getPrevTrainer(),
                                                                            new ArrayList<>());
        trainingList.add(training);
        prevTrainer_trainings.put(training.getPrevTrainer(), trainingList);
      }
    }
    if(notify && this.enableNotify)
      informOfGrabbing(prevTrainer_trainings);
  }

  private void grabTraining(Training training) {
    training.setIsFree(false);
    User user = userService.getAuthUser();
    User prevTrainer = training.getTrainer();
    training.setTrainer(user);
    training.setPrevTrainer(prevTrainer);
    saveTraining(training);
  }

  private void informOfFreeing(List<Training> trainings) {
    String msg = createFreeingMsg(trainings);
    //for(User trainer : userService.getAllTrainer())
    //sendToUser(trainer, msg);
    sendToUser(userService.loadUser("admin"), msg);
  }

  private void informOfGrabbing(Map<User, List<Training>> prevTrainer_trainings) {
    for(Entry<User, List<Training>> e: prevTrainer_trainings.entrySet()) {
      String msg = createGrabbingMsg(e.getValue());
      User prevTrainer = e.getKey();
      sendToUser(prevTrainer, msg);
    }
  }

  private String createFreeingMsg(List<Training> trainings) {
    User trainer = userService.getAuthUser();
    String msg = trainer.getFirstName() + " " + trainer.getLastName() + " braucht Hilfe " +
      "bei folgenden Trainings:\n\n";

    sortTrainingsByDateAsc(trainings);
    msg += listTrainingsWithDate(trainings);

    msg += "\nBitte übernimm die Trainings, wenn du Zeit hast! " + trainer.getFirstName() + " " +
           "gibt dafür einen aus...\n" + "www.ahmacademy.at/ahmnet/vacationtable";
    return msg;
  }

  private String createGrabbingMsg(List<Training> trainings) {
    User currUser = userService.getAuthUser();
    String msg = currUser.getFirstName() + " " + currUser.getLastName() + " hat folgende Trainings " 
                 + "von dir übernommen:\n\n";

    sortTrainingsByDateAsc(trainings);
    msg += listTrainingsWithDate(trainings);

    msg += "\n" + currUser.getFirstName() + " hat was gut bei dir!";
    return msg;
  }

  private void sortTrainingsByDateAsc(List<Training> trainings) {
    Collections.sort(trainings, new Comparator<Training>() {
      @Override
      public int compare(Training tr1, Training tr2) {
        return tr1.getDateTime().isBefore(tr2.getDateTime()) ? -1 : 1;
      }
    });
  }

  private String listTrainingsWithDate(List<Training> trainings) {
    String listing = "";
    for(Training t: trainings) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E dd-MM-yyyy")
                                                     .withLocale(Locale.GERMAN);
      String date = t.getDateTime().toLocalDate().format(formatter);
      String startTime = t.getDateTime().toLocalTime().toString();
      String endTime = t.getDateTime().toLocalTime().plusMinutes(t.getDurationMinutes()).toString();
      listing += "- Am " + date + " von " + startTime + " bis " + endTime + "\n";
    }
    return listing;
  }

  private void sendToUser(User user, String msg) {
    String phone = user.getPhone();
    SmsRequest sms = new SmsRequest(phone, msg);
    smsService.sendWAMessage(sms);
  }


  /* AUTHENTICATION */

  public boolean hasTrainer(Training t, String userId) {
    return userId.equals(t.getTrainer().getId());
  }

  public boolean isFree(Training training) {
    return training.getIsFree();
  }


  
  
  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Autowired
  public void setTrainingRepo(TrainingRepository trainingRepo) {
    this.trainingRepo = trainingRepo;
  }

  @Autowired
  public void setSmsService(SmsService smsService) {
    this.smsService = smsService;
  }

}