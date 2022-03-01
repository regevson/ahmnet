package at.ahmacademy.ahmnet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.model.User;

public interface TrainingRepository extends AbstractRepository<Training, Long> {

    List<Training> findByTrainer_UsernameOrderByDateTimeAsc(String username);

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

    @Query("select t "
    	+ "from Training t "
    	+ "where t.weekNum = :weekNum and t.isFree = true and t.trainer.username != :username "
    	+ "order by t.dateTime asc")
    List<Training> findFreeTrainingsByWeekAndExcludingTrainer(@Param("username") String username, @Param("weekNum") int weekNum);

}
