package at.ahmacademy.ahmnet.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import at.ahmacademy.ahmnet.model.Training;

public interface TrainingRepository extends AbstractRepository<Training, Long>,
                                            JpaSpecificationExecutor<Training> {

}

