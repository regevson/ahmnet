package at.ahmacademy.ahmnet.services;

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
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.ahmacademy.ahmnet.model.SmsRequest;
import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.repositories.TrainingRepository;
import at.ahmacademy.ahmnet.repositories.TrainingSpecification;

@Service
@Scope("application")
public class TrainingService {

    @Autowired
    private UserService userService;
    @Autowired
    private TrainingRepository trainingRepo;
    @Autowired
    private SmsService smsService;


    private Training loadTrainingById(Long trainingId) {
	return trainingRepo.findById(trainingId).orElse(null);
    }

    public Training loadTrainingById(String trainerId, Long trainingId) {
	Training training = loadTrainingById(trainingId);
	authorizeTrainingOwner(trainerId, training);
	return training;
    }

    private Training saveTraining(Training training) {
        return trainingRepo.save(training);
    }

    public void updateTraining(String trainerId, Long trainingId, Training training) {
	authorizeTraining(trainingId, training);
	saveTraining(training);
    }

    private void deleteTraining(Training training) {
	training.getAttendees().removeAll(training.getAttendees());
	training.getTrainingGroup().getTrainings().remove(training);
        trainingRepo.delete(training);
    }

    @Transactional
    public void deleteTrainings(String trainerId, Long[] trainingIds) {
	authorizeTrainingOwner(trainerId, trainingIds);

	for(int i = 0; i < trainingIds.length; i++) {
	    Training t = loadTrainingById(trainingIds[i]);
	    deleteTraining(t);
	}
    }

    public List<List<Training>> groupByDay(List<Training> trainings) {
	List<List<Training>> trainingsByDay = new ArrayList<>();
	for(int i = 0; i < 7; i++) // init list
	   trainingsByDay.add(new ArrayList<>()); 

	for(Training training : trainings) {
	    int day = training.getDateTime().getDayOfWeek().getValue()-1;
	    trainingsByDay.get(day).add(training);
	}
	
	return trainingsByDay;
    }
    
    public List<String> getDatesInWeek(int weekNum) {
        LocalDate monday = LocalDate.of(Year.now().getValue(), 2, 1)
                .with(WeekFields.of(Locale.GERMANY).getFirstDayOfWeek())
                .with(WeekFields.of(Locale.GERMANY).weekOfWeekBasedYear(), weekNum);
        return IntStream.range(0, 7).mapToObj(monday::plusDays)
        	.map(d -> d.toString())
        	.collect(Collectors.toList());
    }

    private void freeTraining(Training training) {
	training.setIsFree(true);
	training.setTrainer(training.getPrevTrainer());
	saveTraining(training);
    }

    private void grabTraining(Training training) {
	training.setIsFree(false);
	User user = userService.getAuthenticatedUser();
	User prevTrainer = training.getTrainer();
	training.setTrainer(user);
	training.setPrevTrainer(prevTrainer);
	saveTraining(training);
    }

    @PreAuthorize("#trainerId eq #training.getTrainer.getId() && (hasAuthority('ADMIN') or authentication.getName() eq #trainerId)")
    public void saveNewTraining(String trainerId, Training training) {

	if(training.getLastDate() == null)
            saveTraining(training);
	else
	    saveRecurringTraining(trainerId, training);
    }

    @Transactional
    private List<Training> saveRecurringTraining(String trainerId, Training training) {
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
	Specification<Training> spec = Specification.where(TrainingSpecification.hasWeekNum(weekNum))
		.and(TrainingSpecification.hasStatus(true))
		.and(exclId.isPresent() ? TrainingSpecification.exclId(exclId.get()) : null);
	List<Training> trainings = this.trainingRepo.findAll(spec);
	return groupByDay(trainings);
    }

    @PreAuthorize("hasAuthority('ADMIN') or authentication.getName() eq #trainerId")
    public List<List<Training>> loadTrainingsByTrainer(String trainerId, 
	    					       Integer weekNum, 
	    					       Optional<Boolean> isFree) {

	Specification<Training> spec = Specification.where(TrainingSpecification.hasWeekNum(weekNum))
		.and(TrainingSpecification.hasTrainer(trainerId))
		.and(isFree.isPresent() ? TrainingSpecification.hasStatus(isFree.get()) : null);
	List<Training> trainings = this.trainingRepo.findAll(spec);
	return groupByDay(trainings);
    }


    @Transactional
    public void freeTrainings(String trainerId, Long[] trainingIds, boolean notify) {
	authorizeTrainingOwner(trainerId, trainingIds);

	List<Training> trainingList = new ArrayList<>();
	for(int i = 0; i < trainingIds.length; i++) {
	    Training t = loadTrainingById(trainingIds[i]);
	    freeTraining(t);

	    if(notify)
                trainingList.add(t);
	}
	if(notify && false)
            informOfFreeing(trainingList);
    }

    @Transactional
    public void grabTrainings(Long[] trainingIds, boolean notify) {
	authorizeGrabTraining(trainingIds);

	Map<User, List<Training>> prevTrainer_trainings = new HashMap<>();
	for(int i = 0; i < trainingIds.length; i++) {
	    Training t = loadTrainingById(trainingIds[i]);
	    grabTraining(t);

	    if(notify) {
                List<Training> trainingList = prevTrainer_trainings.getOrDefault(t.getPrevTrainer(), new ArrayList<>());
                trainingList.add(t);
                prevTrainer_trainings.put(t.getPrevTrainer(), trainingList);
	    }
	}
	if(notify && false)
	    informOfGrabbing(prevTrainer_trainings);
    }

    private void informOfFreeing(List<Training> trainings) {
	String msg = createFreeingMsg(trainings);
	//for(User trainer : userService.getAllTrainer())
            //sendToUser(trainer, msg);
        sendToUser(userService.loadUser("admin"), msg);
    }

    private void informOfGrabbing(Map<User, List<Training>> prevTrainer_trainings) {
	for(Entry<User, List<Training>> e : prevTrainer_trainings.entrySet()) {
            String msg = createGrabbingMsg(e.getValue());
            User prevTrainer = e.getKey();
            sendToUser(prevTrainer, msg);
	}
    }

    private String createFreeingMsg(List<Training> trainings) {
        User trainer = userService.getAuthenticatedUser();
	String msg = trainer.getFirstName() + " " + trainer.getLastName() + " braucht Hilfe "
		+ "bei folgenden Trainings:\n\n";

        sortTrainingsByDateAsc(trainings);
        msg += listTrainingsWithDate(trainings);

	msg += "\nBitte übernimm die Trainings, wenn du Zeit hast! " + trainer.getFirstName() + " "
		+ "gibt dafür einen aus...\n"
		+ "www.ahmacademy.at/ahmnet/vacationtable";
	return msg;
    }

    private String createGrabbingMsg(List<Training> trainings) {
        User currUser = userService.getAuthenticatedUser();
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
	for(Training t : trainings) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E dd-MM-yyyy").withLocale(Locale.GERMAN);
	    String date = t.getDateTime().toLocalDate().format(formatter);
	    String startTime = t.getDateTime().toLocalTime().toString();
	    String endTime = t.getDateTime().toLocalTime().plusMinutes(t.getDurationMinutes()).toString();
	    listing += "- Am " + date + " von " + startTime 
		    	+ " bis " + endTime + "\n";
	}
	return listing;
    }
    
    private void sendToUser(User user, String msg) {
        String phone = user.getPhone();
        SmsRequest sms = new SmsRequest(phone, msg);
        smsService.sendWAMessage(sms);
    }



// Authorization

    private void authorizeTrainingOwner(String pathTrainerId, Long pathTrainingId, Training training) {
	String trainerId = training.getTrainer().getId();
	if(!pathTrainerId.equals(trainerId))
	    throw new IllegalArgumentException("Trainer in path is not trainer of this training!");
	if(!pathTrainingId.equals(training.getId()))
	    throw new IllegalArgumentException("Training in path is not this training!");
	if(!userService.isAdmin() && !userService.isAuthUser(trainerId) && !training.getIsFree())
	    throw new AccessDeniedException("Given trainer is not trainer of this training!");
    }
    private void authorizeTrainingOwner(String pathTrainerId, Training training) {
        authorizeTrainingOwner(pathTrainerId, training.getId(), training);
    }
    private void authorizeTrainingOwner(String pathTrainerId, Long[] trainingIds) {
	for(int i = 0; i < trainingIds.length; i++)
            authorizeTrainingOwner(pathTrainerId, loadTrainingById(trainingIds[i]));
    }

    private void authorizeTraining(Long pathTrainingId, Training training) {
	Long trainingId = training.getId();
	String trainerId = training.getTrainer().getId();
	if(!pathTrainingId.equals(trainingId))
	    throw new IllegalArgumentException("Training in path is not this training!");
	if(!userService.isAdmin() && !userService.isAuthUser(trainerId))
	    throw new AccessDeniedException("Given trainer is not trainer of this training!");
    }

    private void authorizeGrabTraining(Training training) {
	if((!userService.isAdmin() && !training.getIsFree()) || !userService.hasTrainerRights())
	    throw new AccessDeniedException("No rights to free training!");
    }

    private void authorizeGrabTraining(Long[] trainingIds) {
	for(int i = 0; i < trainingIds.length; i++)
            authorizeGrabTraining(loadTrainingById(trainingIds[i]));
    }

}
