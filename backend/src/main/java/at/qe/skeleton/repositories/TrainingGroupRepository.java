package at.qe.skeleton.repositories;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import at.qe.skeleton.model.TrainingGroup;

@Repository
public interface TrainingGroupRepository extends AbstractRepository<TrainingGroup, Long> {

    TrainingGroup findById(Long id);

    Set<TrainingGroup> findByTrainer_Username(String username);

    Set<TrainingGroup> findByClub_NameContaining(String name);

    @Query("select count(t) "
    	+ "from Training t join t.trainingGroup "
    	+ "where t.trainingGroup.id = :trainingGroupId and t.dateTime < :currentDayTime")
    int countPlayedTrainingsByGroupId(@Param("trainingGroupId") long trainingGroupId, @Param("currentDayTime") LocalDateTime currentDayTime);

}