package at.ahmacademy.ahmnet.tests.trainingGroup;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import at.ahmacademy.ahmnet.model.TrainingGroup;
import at.ahmacademy.ahmnet.repositories.TrainingGroupRepository;

@SpringBootTest
@WebAppConfiguration
public class TrainingGroupRepositoryTest {

/*
  @Autowired
  TrainingGroupRepository trainingGroupRepo;

  @Test
  public void testFindByTrainer_Username() {
    Set<TrainingGroup> groups = this.trainingGroupRepo.findByTrainer_Username("johndoe");
    Assertions.assertTrue(groups.size() > 0, "Too few groups");
    for(TrainingGroup g: groups)
      Assertions.assertEquals("johndoe", g.getTrainer().getId(), "Wrong trainer in group");
  }

  @Test
  public void testFindByClub_NameContaining() {
    Set<TrainingGroup> groups = this.trainingGroupRepo.findByClub_NameContaining("TC Wiesing");
    Assertions.assertTrue(groups.size() > 0, "Too few groups");
    for(TrainingGroup g: groups)
      Assertions.assertEquals("TC Wiesing", g.getClub().getName(), "Wrong club in group");
  }

  @Test
  public void testCountPlayedTrainingsByGroupId() {
    int numGroups = trainingGroupRepo.countPlayedTrainingsByGroupId(0L, LocalDateTime.now());
    Assertions.assertEquals(2, numGroups, "Wrong number of played trainings of group");
  }
  */

}