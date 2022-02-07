package at.qe.skeleton.services;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import at.qe.skeleton.model.Training;
import at.qe.skeleton.model.TrainingGroup;
import at.qe.skeleton.model.User;
import at.qe.skeleton.repositories.TrainingRepository;

@Service
@Scope("application")
public class TrainingService {

    @Autowired
    private TrainingRepository trainingRepository;

    @PreAuthorize("hasAnyAuthority('ADMIN','TRAINER')")
    public Training loadTraining(long id) {
	return this.trainingRepository.findById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','TRAINER')")
    public List<Training> loadTrainingsByTrainingGroup(TrainingGroup group) {
	return this.trainingRepository.findByTrainingGroupIdOrderByDateTimeAsc(group.getId());
    }

    public List<Training> loadTrainingsByPlayer(User player) {
	return this.trainingRepository.findByPlayerId(player.getId());
    }

    public List<Training> loadTrainingsByPlayerAndWeek(User player, int weekNum) {
	return this.trainingRepository.findByPlayerIdAndWeek(player.getId(), weekNum);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','TRAINER')")
    public List<Training> loadTrainingsByTrainer(User trainer) {
	return this.trainingRepository.findByTrainerId(trainer.getId());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','TRAINER')")
    public List<Training> loadTrainingsByTrainerAndWeek(String trainerUsername, int weekNum) {
	return this.trainingRepository.findByTrainerIdAndWeek(trainerUsername, weekNum);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','TRAINER')")
    public Training saveTraining(Training training) {
        return trainingRepository.save(training);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','TRAINER')")
    public void deleteTraining(Training training) {
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

}
