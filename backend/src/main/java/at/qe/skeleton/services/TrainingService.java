package at.qe.skeleton.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.qe.skeleton.model.SmsRequest;
import at.qe.skeleton.model.Training;
import at.qe.skeleton.model.User;
import at.qe.skeleton.repositories.TrainingRepository;

@Service
@Scope("application")
public class TrainingService {

    @Autowired
    private UserService userService;
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private SmsService smsService;

    @PreAuthorize("hasAnyAuthority('ADMIN','TRAINER')")
    public Training loadTrainingById(long id) {
	return this.trainingRepository.findById(id);
    }

/*
    @PreAuthorize("hasAnyAuthority('ADMIN','TRAINER')")
    public List<Training> loadTrainingsByTrainingGroup(TrainingGroup group) {
	return this.trainingRepository.findByTrainingGroupIdOrderByDateTimeAsc(group.getId());
    }
    */

    public List<Training> loadTrainingsByPlayer(User player) {
	return this.trainingRepository.findByPlayerId(player.getId());
    }

    public List<Training> loadTrainingsByPlayerAndWeek(User player, int weekNum) {
	return this.trainingRepository.findByPlayerIdAndWeek(player.getId(), weekNum);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','TRAINER')")
    public List<Training> loadTrainingsByTrainer(User trainer) {
	return this.trainingRepository.findByTrainer_UsernameOrderByDateTimeAsc(trainer.getId());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','TRAINER')")
    public List<Training> loadTrainingsByTrainerAndWeek(String trainerUsername, int weekNum) {
	return this.trainingRepository.findByTrainerIdAndWeek(trainerUsername, weekNum);
    }

    @PreAuthorize("hasAuthority('ADMIN') or authentication.getName() eq #training.trainer.getId")
    public Training saveTraining(Training training) {
        return trainingRepository.save(training);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or authentication.getName() eq #training.trainer.getId")
    public void deleteTraining(Training training) {
	training.getAttendees().removeAll(training.getAttendees());
	training.getTrainingGroup().getTrainings().remove(training);
        trainingRepository.delete(training);
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
    
    public List<Training> orderTrainingsAsc(Set<Training> trs) {
        List<Training> trList = new ArrayList<>(trs);
        Comparator<Training> comparator = Comparator.comparing(Training::getDateTime);
        trList.sort(comparator);
        return trList;
    }

    public List<String> getDatesInWeek(int weekNum) {
        LocalDate monday = LocalDate.of(Year.now().getValue(), 2, 1)
                .with(WeekFields.of(Locale.GERMANY).getFirstDayOfWeek())
                .with(WeekFields.of(Locale.GERMANY).weekOfWeekBasedYear(), weekNum);
        return IntStream.range(0, 7).mapToObj(monday::plusDays)
        	.map(d -> d.toString())
        	.collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public List<List<Training>> loadFreeTrainingsByWeek(int weekNum) {
	List<Training> trainings = this.trainingRepository.findFreeTrainingsByWeek(weekNum);
	return this.groupByDay(trainings);
    }

    @PreAuthorize("hasAuthority('ADMIN') or authentication.getName() eq #training.trainer.getId")
    public void freeTraining(Training training) {
	training.setIsFree(true);
	training.setTrainer(training.getOriginalTrainer());
	this.saveTraining(training);
    }

    @PreAuthorize("hasAuthority('ADMIN') or #training.getIsFree()")
    public void grabTraining(Training training) {
	training.setIsFree(false);
	User user = this.userService.getAuthenticatedUser();
	training.setTrainer(user);
	this.saveTraining(training);
    }

    @PreAuthorize("hasAuthority('ADMIN') or authentication.getName() eq #trainerId")
    public List<List<Training>> getTrainingsByWeek(String trainerId, int weekNum) {
        List<Training> trainings = this.loadTrainingsByTrainerAndWeek(trainerId, weekNum);
	return this.groupByDay(trainings);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or authentication.getName() eq #trainerId")
    public void saveRecurringTrainings(Training training) {
	LocalDate lastDate = training.getLastDate();
	if(lastDate == null) {
            saveTraining(training);
            return;
	}

	LocalDate startDate = training.getDateTime().toLocalDate();
	LocalTime startTime = training.getDateTime().toLocalTime();
	while(startDate.isBefore(lastDate) || startDate.isEqual(lastDate)) {
            Training tmp = new Training(training);
            tmp.setDateTime(startDate.atTime(startTime));
            this.saveTraining(tmp);
            startDate = startDate.plusWeeks(1);
	}
	
    }

    @Transactional
    public void deleteTrainings(Long[] trainingIds) {
	authorizeTrainingOwner(trainingIds);

	for(int i = 0; i < trainingIds.length; i++) {
	    Training t = loadTrainingById(trainingIds[i]);
	    deleteTraining(t);
	}
    }

    @Transactional
    public void freeTrainings(Long[] trainingIds) {
	authorizeTrainingOwner(trainingIds);

	List<Training> trainingList = new ArrayList<>();
	for(int i = 0; i < trainingIds.length; i++) {
	    Training t = loadTrainingById(trainingIds[i]);
	    freeTraining(t);

	    trainingList.add(t);
	}
	//informOfFreeing(trainingList);
    }

    @Transactional
    public void grabTrainings(Long[] trainingIds) {
	authorizeGrabTraining(trainingIds);

	Map<User, List<Training>> prevTrainer_trainings = new HashMap<>();
	for(int i = 0; i < trainingIds.length; i++) {
	    Training t = loadTrainingById(trainingIds[i]);
	    grabTraining(t);

	    List<Training> trainingList = prevTrainer_trainings.getOrDefault(t.getOriginalTrainer(), new ArrayList<>());
	    trainingList.add(t);
	    prevTrainer_trainings.put(t.getOriginalTrainer(), trainingList);
	}
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
        User trainer = this.userService.getAuthenticatedUser();
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
        User currUser = this.userService.getAuthenticatedUser();
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

    private void authorizeTrainingOwner(Training training) {
	User currentUser = this.userService.getAuthenticatedUser();
	if(!userService.isAdmin() && currentUser.compareTo(training.getTrainer()) != 0)
	    throw new AccessDeniedException("Logged in user is not trainer of this training!");
    }

    private void authorizeTrainingOwner(Long[] trainingIds) {
	for(int i = 0; i < trainingIds.length; i++)
            this.authorizeTrainingOwner(loadTrainingById(i));
    }

    private void authorizeGrabTraining(Training training) {
	if((!userService.isAdmin() && !training.getIsFree()) || !userService.hasTrainerRights())
	    throw new AccessDeniedException("No rights to free training!");
    }

    private void authorizeGrabTraining(Long[] trainingIds) {
	for(int i = 0; i < trainingIds.length; i++)
            this.authorizeGrabTraining(loadTrainingById(trainingIds[i]));
    }


}
