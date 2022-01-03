package at.qe.skeleton.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import at.qe.skeleton.model.TrainingGroup;

@Repository
public interface TrainingGroupRepository extends AbstractRepository<TrainingGroup, Long> {

    TrainingGroup findById(Long id);

    Set<TrainingGroup> findByTrainer_Username(String username);

    @Query("select g "
    	+ "from TrainingGroup g join g.players players "
    	+ "where players.username=:username")
    Set<TrainingGroup> findByPlayer_Username(@Param("username") String username);

}

