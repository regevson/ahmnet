package at.qe.skeleton.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.qe.skeleton.model.Training;

public interface TrainingRepository extends AbstractRepository<Training, Long> {

    Training findById(long trainingId);

    List<Training> findByTrainingGroupIdOrderByDateTimeAsc(long trainingGroupId);

    List<Training> findByIsFreeTrueOrderByDateTimeAsc();

    @Query("select t "
    	+ "from Training t join t.trainingGroup g join g.players p "
    	+ "where p.username = :username "
    	+ "order by t.dateTime asc")
    List<Training> findByPlayerId(@Param("username") String playerUsername);

    List<Training> findByTrainer_UsernameOrderByDateTimeAsc(String username);

    @Query("select t "
    	+ "from Training t join t.trainingGroup g join g.players p "
    	+ "where p.username = :username and t.weekNum = :weekNum and t.isFree = false "
    	+ "order by t.dateTime asc")
    List<Training> findByPlayerIdAndWeek(@Param("username") String playerUsername, @Param("weekNum") int weekNum);

    @Query("select t "
    	+ "from Training t "
    	+ "where t.trainer.username = :username and t.weekNum = :weekNum and t.isFree = false "
    	+ "order by t.dateTime asc")
    List<Training> findByTrainerIdAndWeek(@Param("username") String trainerUsername, @Param("weekNum") int weekNum);

    @Query("select t "
    	+ "from Training t "
    	+ "where t.weekNum = :weekNum and t.isFree = true "
    	+ "order by t.dateTime asc")
    List<Training> findFreeTrainingsByWeek(@Param("weekNum") int weekNum);

/*
    @Transactional
    @Modifying
    @Query("update Training t "
    	+ "set t.attendees = :attendees, t.bulletPoints = :bulletPoints, t.comment = :comments "
    	+ "where t.id = :id ")
    void updateTrainingDetails(@Param("id") long id, @Param("attendees") Set<User> attendees, @Param("bulletPoints") String bulletPoints, @Param("comments") String comments);
    */

}
