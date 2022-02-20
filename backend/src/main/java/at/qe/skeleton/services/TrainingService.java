package at.qe.skeleton.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
	for(int i = 0; i < trainingIds.length; i++) {
	    Training t = loadTrainingById(trainingIds[i]);
	    deleteTraining(t);
	}
    }

    @Transactional
    public void freeTrainings(Long[] trainingIds) {
	List<Training> list = new ArrayList<>();
	for(int i = 0; i < trainingIds.length; i++) {
	    Training t = loadTrainingById(trainingIds[i]);
	    freeTraining(t);
	    list.add(t);
	}

	informOfFreeing(list);
    }

    private void informOfFreeing(List<Training> list) {
	String msg = createMsg(list);
	sendToParticipants(msg);
    }

    private String createMsg(List<Training> list) {
        User trainer = this.userService.getAuthenticatedUser();
	String msg = trainer.getFirstName() + " " + trainer.getLastName() + " braucht Hilfe "
		+ "bei folgenden Trainings:\n\n";

        Collections.sort(list, new Comparator<Training>() {
            @Override
            public int compare(Training tr1, Training tr2) {
                return tr1.getDateTime().isBefore(tr2.getDateTime()) ? -1 : 1;
            }
        });

	for(Training t : list) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E dd-MM-yyyy").withLocale(Locale.GERMAN);
	    String date = t.getDateTime().toLocalDate().format(formatter);
	    String startTime = t.getDateTime().toLocalTime().toString();
	    String endTime = t.getDateTime().toLocalTime().plusMinutes(t.getDurationMinutes()).toString();
	    msg += "- Am " + date + " von " + startTime 
		    	+ " bis " + endTime + "\n";
	}
	msg += "\nBitte übernimm die Trainings, wenn du Zeit hast!\n"
		+ "www.ahmacademy.at/ahmnet/vacationtable";
	return msg;
    }

    private void sendToParticipants(String msg) {
	/* TODO: get all trainers/admins */
	User admin = this.userService.loadUser("admin");
	String phone = admin.getPhone();
        smsService.sendWAMessage(new SmsRequest(phone, msg));
    }

    @Transactional
    public void grabTrainings(Long[] trainingIds) {
	for(int i = 0; i < trainingIds.length; i++) {
	    Training t = loadTrainingById(trainingIds[i]);
	    grabTraining(t);
	}
    }

}
