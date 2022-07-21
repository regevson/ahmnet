package at.ahmacademy.ahmnet.tests.trainingGroup;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.model.TrainingGroup;
import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.repositories.TrainingGroupRepository;
import at.ahmacademy.ahmnet.services.trainingGroup.TrainingGroupService;

@SpringBootTest
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class TrainingGroupServiceTest {

  @Autowired
  TrainingGroupRepository groupRepo;

  TrainingGroupService groupService;
  @BeforeEach
  public void initTrainingService() {
    groupService = new TrainingGroupService();
    groupService.setGroupRepository(groupRepo);
  }

  @Transactional
  @Test
  @DirtiesContext
  @WithMockUser(username = "admin", authorities = {"ADMIN"})
  public void testDeleteTrainingGroup() {
    Long id = 1L;
    TrainingGroup g = groupService.loadGroupById(id);
    groupService.deleteGroup(g);
    Assertions.assertNull(groupService.loadGroupById(id));
  }

  @Test
  @WithMockUser(username = "admin", authorities = {"ADMIN"})
  public void testCalcAttendance() {
    User p1 = User.builder().username("player1").build();
    User p2 = User.builder().username("player2").build();
    TrainingGroup group = TrainingGroup.builder().players(Set.of(p1,p2)).build();
    Training t1 = Training.builder().attendees(Set.of(p1, p2)).build();
    Training t2 = Training.builder().attendees(Set.of(p2)).build();
    Training t3 = Training.builder().attendees(Set.of(p2)).build();
    group.setTrainings(Set.of(t1, t2, t3));
    groupService.calcAttendance(group);
    Assertions.assertEquals(1, group.getAttendance().get("player1"), "Wrong attendance");
    Assertions.assertEquals(3, group.getAttendance().get("player2"), "Wrong attendance");
  }

  @Test
  @WithMockUser(username = "admin", authorities = {"ADMIN"})
  public void testCountPlayedTrainingsByGroupId() {
    TrainingGroup g = groupService.loadGroupById(2L);
    groupService.calcNumPlayedSessions(g);
    Assertions.assertEquals(2, g.getNumPlayedSessions(), "Wrong number of played trainings of group");
  }

/*
  @Test
  public void testGetNumOfGroups() {
    Club c1 = Club.builder().id("Club1").build();
    Club c2 = Club.builder().id("Club2").build();
    TrainingGroup g1 = new TrainingGroup();
    TrainingGroup g2 = new TrainingGroup();
    TrainingGroup g3 = new TrainingGroup();
    doReturn(Set.of(g1)).when(this.groupRepo).findByClub_IdContaining(c1.getId());
    doReturn(Set.of(g2, g3)).when(this.groupRepo).findByClub_IdContaining(c2.getId());

    Map<String, Integer> numGroupsByClub = this.fakeGroupService.getNumOfGroups(List.of(c1, c2));
    Assertions.assertEquals(1, numGroupsByClub.get(c1.getName()), "Wrong group-num");
    Assertions.assertEquals(2, numGroupsByClub.get(c2.getName()), "Wrong group-num");
  }
  */

/*
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
*/
  

}