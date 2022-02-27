package at.qe.skeleton.tests.training;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;

import at.qe.skeleton.model.Training;
import at.qe.skeleton.repositories.TrainingRepository;

@SpringBootTest
@WebAppConfiguration
public class TrainingRepositoryTest {

    @Autowired
    TrainingRepository trainingRepo;
    
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
