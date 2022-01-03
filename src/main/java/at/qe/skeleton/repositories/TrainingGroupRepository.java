package at.qe.skeleton.repositories;

import java.util.Set;

import org.springframework.stereotype.Repository;

import at.qe.skeleton.model.TrainingGroup;

@Repository
public interface TrainingGroupRepository extends AbstractRepository<TrainingGroup, Long> {

    Set<TrainingGroup> findByTrainer_Username(String username);
}
