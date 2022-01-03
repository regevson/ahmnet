package at.qe.skeleton.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import at.qe.skeleton.model.TrainingGroup;
import at.qe.skeleton.model.User;
import at.qe.skeleton.repositories.TrainingGroupRepository;

@Service
@Scope("application")
public class TrainingGroupService {

    @Autowired
    TrainingGroupRepository trainingGroupRepository;

    
    @PreAuthorize("hasAuthority('ADMIN')")
    public Set<TrainingGroup> loadTrainingGroupByTrainer(User trainer) {
	return this.trainingGroupRepository.findByTrainer_Username(trainer.getUsername());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Set<TrainingGroup> loadTrainingGroupByPlayer(User player) {
	return this.trainingGroupRepository.findByPlayer_Username(player.getUsername());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public TrainingGroup loadTrainingGroupById(long id) {
	return this.trainingGroupRepository.findById(id);
    }

    @PreAuthorize("hasAuthority('TRAINER')")
    public TrainingGroup saveGroup(TrainingGroup group) {
        return trainingGroupRepository.save(group);
    }

    @PreAuthorize("hasAuthority('TRAINER')")
    public TrainingGroup deleteGroup(TrainingGroup group) {
        return trainingGroupRepository.save(group);
    }

}
