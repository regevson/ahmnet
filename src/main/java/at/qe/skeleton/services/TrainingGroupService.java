package at.qe.skeleton.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import at.qe.skeleton.model.Trainer;
import at.qe.skeleton.model.TrainingGroup;
import at.qe.skeleton.repositories.TrainingGroupRepository;

@Service
@Scope("application")
public class TrainingGroupService {

    @Autowired
    TrainingGroupRepository trainingGroupRepository;

    public Set<TrainingGroup> loadTrainingGroupByTrainer(Trainer trainer) {
	return this.trainingGroupRepository.findByTrainer_Username(trainer.getUsername());
    }

}
