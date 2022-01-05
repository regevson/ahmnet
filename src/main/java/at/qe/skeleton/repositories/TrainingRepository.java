package at.qe.skeleton.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.qe.skeleton.model.Training;

public interface TrainingRepository extends AbstractRepository<Training, Long> {

    List<Training> findByTrainingGroupIdOrderByStartTimeAsc(long trainingGroupId);

    @Query("select t "
    	+ "from Training t join t.trainingGroup g join g.players p "
    	+ "where p.username = :username "
    	+ "order by t.startTime asc")
    List<Training> findByPlayerId(@Param("username") String playerUsername);

    @Query("select t "
    	+ "from Training t join t.trainingGroup g "
    	+ "where g.trainer.username = :username "
    	+ "order by t.startTime asc")
    List<Training> findByTrainerId(@Param("username") String trainerUsername);

    @Query("select t "
    	+ "from Training t join t.trainingGroup g join g.players p "
    	+ "where p.username = :username and t.weekNum = :weekNum "
    	+ "order by t.startTime asc")
    List<Training> findByPlayerIdAndWeek(@Param("username") String playerUsername, @Param("weekNum") int weekNum);

    @Query("select t "
    	+ "from Training t join t.trainingGroup g "
    	+ "where g.trainer.username = :username and t.weekNum = :weekNum "
    	+ "order by t.startTime asc")
    List<Training> findByTrainerIdAndWeek(@Param("username") String trainerUsername, @Param("weekNum") int weekNum);

}