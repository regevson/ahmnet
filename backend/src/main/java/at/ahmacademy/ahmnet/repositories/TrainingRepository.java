package at.ahmacademy.ahmnet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ahmacademy.ahmnet.model.Training;

public interface TrainingRepository extends AbstractRepository<Training, Long>, JpaSpecificationExecutor<Training> {

}

