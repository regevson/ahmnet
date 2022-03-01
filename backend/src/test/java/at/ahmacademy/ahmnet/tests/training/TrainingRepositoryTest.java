package at.ahmacademy.ahmnet.tests.training;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.repositories.TrainingRepository;

@SpringBootTest
@WebAppConfiguration
public class TrainingRepositoryTest {

    @Autowired
    TrainingRepository trainingRepo;
    

    @Test
    public void testFindByTrainer_UsernameOrderByDateTimeAsc() {
	List<Training> trainings = trainingRepo.findByTrainer_UsernameOrderByDateTimeAsc("johndoe");
        Assertions.assertTrue(trainings.size() > 2, "Too few trainings");
        LocalDateTime dateTime1 = trainings.get(0).getDateTime();
        LocalDateTime dateTime2 = trainings.get(1).getDateTime();
        LocalDateTime dateTime3 = trainings.get(2).getDateTime();
        Assertions.assertTrue(dateTime1.isBefore(dateTime2), "Trainings are not sorted correctly by time");
        Assertions.assertTrue(dateTime2.isBefore(dateTime3), "Trainings are not sorted correctly by time");
    }

    @Test
    public void testFindByTrainerIdAndWeek() {
	List<Training> trainings = trainingRepo.findByTrainerIdAndWeek("johndoe", 8);
        Assertions.assertTrue(trainings.size() > 0, "Too few trainings");
	for(Training t : trainings)
	    Assertions.assertEquals("johndoe", t.getTrainer().getId(), "Training doesn't belong to trainer");
    }

    @Test
    public void testFindFreeTrainingsByWeek() {
	List<Training> trainings = trainingRepo.findFreeTrainingsByWeek(8);
        Assertions.assertTrue(trainings.size() > 0, "Too few trainings");
	for(Training t : trainings)
	    Assertions.assertTrue(t.getIsFree(), "Trainings is not free");
    }

    @Test
    public void testFindFreeTrainingsByWeekAndExcluding() {
	List<Training> trainings = trainingRepo.findFreeTrainingsByWeekAndExcludingTrainer("johndoe", 9);
        Assertions.assertTrue(trainings.size() > 0, "Too few trainings");
	for(Training t : trainings) {
	    Assertions.assertTrue(t.getIsFree(), "Trainings is not free");
	    Assertions.assertNotEquals("johndoe", t.getTrainer().getId(), "Trainer was not exluded");
	}
    }

}
