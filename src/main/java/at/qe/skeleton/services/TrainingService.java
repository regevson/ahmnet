package at.qe.skeleton.services;

import java.util.List;

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
    public List<Training> loadTrainingsByTrainingGroup(TrainingGroup group) {
	return this.trainingRepository.findByTrainingGroupIdOrderByStartTimeAsc(group.getId());
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
    public List<Training> loadTrainingsByTrainerAndWeek(User trainer, int weekNum) {
	return this.trainingRepository.findByTrainerIdAndWeek(trainer.getId(), weekNum);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','TRAINER')")
    public Training saveTraining(Training training) {
        return trainingRepository.save(training);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','TRAINER')")
    public void deleteTraining(Training training) {
        trainingRepository.delete(training);
    }

}
