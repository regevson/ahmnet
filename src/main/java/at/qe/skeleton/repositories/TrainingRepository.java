package at.qe.skeleton.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import at.qe.skeleton.model.Training;
import at.qe.skeleton.model.User;

public interface TrainingRepository extends AbstractRepository<Training, Long> {

    List<Training> findByTrainingGroupIdOrderByDateTimeAsc(long trainingGroupId);

    @Query("select t "
    	+ "from Training t join t.trainingGroup g join g.players p "
    	+ "where p.username = :username "
    	+ "order by t.dateTime asc")
    List<Training> findByPlayerId(@Param("username") String playerUsername);

    @Query("select t "
    	+ "from Training t join t.trainingGroup g "
    	+ "where g.trainer.username = :username "
    	+ "order by t.dateTime asc")
    List<Training> findByTrainerId(@Param("username") String trainerUsername);

    @Query("select t "
    	+ "from Training t join t.trainingGroup g join g.players p "
    	+ "where p.username = :username and t.weekNum = :weekNum "
    	+ "order by t.dateTime asc")
    List<Training> findByPlayerIdAndWeek(@Param("username") String playerUsername, @Param("weekNum") int weekNum);

    @Query("select t "
    	+ "from Training t join t.trainingGroup g "
    	+ "where g.trainer.username = :username and t.weekNum = :weekNum "
    	+ "order by t.dateTime asc")
    List<Training> findByTrainerIdAndWeek(@Param("username") String trainerUsername, @Param("weekNum") int weekNum);

    @Transactional
    @Modifying
    @Query("update Training t "
    	+ "set t.attendees = :attendees, t.bulletPoints = :bulletPoints, t.comment = :comments "
    	+ "where t.id = :id ")
    void updateTrainingDetails(@Param("id") long id, @Param("attendees") Set<User> attendees, @Param("bulletPoints") String bulletPoints, @Param("comments") String comments);

}
