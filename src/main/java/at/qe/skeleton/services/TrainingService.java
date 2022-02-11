package at.qe.skeleton.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.qe.skeleton.model.Training;
import at.qe.skeleton.model.User;
import at.qe.skeleton.repositories.TrainingRepository;

@Service
@Scope("application")
public class TrainingService {

    @Autowired
    private TrainingRepository trainingRepository;

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

    @PreAuthorize("hasAnyAuthority('ADMIN','TRAINER')")
    public Training saveTraining(Training training) {
        return trainingRepository.save(training);
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public void deleteTraining(long id) {
	Training training = this.loadTrainingById(id);
	deleteTraining(training);
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public void deleteTraining(Training training) {
	training.getAttendees().removeAll(training.getAttendees());
	training.getTrainingGroup().getTrainings().remove(training);
        trainingRepository.delete(training);
    }

    public HashMap<DayOfWeek, List<Training>> groupByDay(List<Training> trainings) {
	HashMap<DayOfWeek, List<Training>> group = new HashMap<>();
	for(Training training : trainings) {
	    DayOfWeek day = training.getDateTime().getDayOfWeek();
	    if(group.get(day) == null)
		group.put(day, new ArrayList<>());
	    List<Training> dayList = group.get(day);
	    dayList.add(training);
	    group.put(day, dayList);
	}
	return group;
    }
    
    public List<Training> orderTrainingsAsc(Set<Training> trs) {
        List<Training> trList = new ArrayList<>(trs);
        Comparator<Training> comparator = Comparator.comparing(Training::getDateTime);
        trList.sort(comparator);
        return trList;
    }

    public List<String> getDatesInWeek(int weekNum) {
        LocalDate monday = LocalDate.ofYearDay(2022,1)
                .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, weekNum)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate prevSunday = monday.minusDays(1);
        return IntStream.range(0, 7).mapToObj(prevSunday::plusDays)
        	.map(d -> this.convertDateToGerman(d))
        	.collect(Collectors.toList());
    }

    public String convertDateToGerman(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);
    }
    


}
