package at.qe.skeleton.tests.trainingGroup;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import at.qe.skeleton.model.TrainingGroup;
import at.qe.skeleton.repositories.TrainingGroupRepository;

@SpringBootTest
@WebAppConfiguration
public class TrainingGroupRepositoryTest {

    @Autowired
    TrainingGroupRepository trainingGroupRepo;
    
    @Test
    public void testCountPlayedTrainingsByGroupId() {
	TrainingGroup group = this.trainingGroupRepo.findById(0L);
	int numGroups = trainingGroupRepo.countPlayedTrainingsByGroupId(group.getId(), LocalDateTime.now());
        Assertions.assertEquals(2, numGroups, "Played trainings of group is wrong");
    }

}
