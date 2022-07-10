package at.ahmacademy.ahmnet.tests.trainingGroup;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import at.ahmacademy.ahmnet.model.Club;
import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.model.TrainingGroup;
import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.repositories.TrainingGroupRepository;
import at.ahmacademy.ahmnet.services.trainingGroup.TrainingGroupService;
import at.ahmacademy.ahmnet.services.user.UserService;

@SpringBootTest
@WebAppConfiguration
public class TrainingGroupServiceTest {

  @Mock
  TrainingGroupRepository groupRepo;

  @Mock
  UserService userService;

  @InjectMocks
  TrainingGroupService groupService;

  @Test
  public void testDeleteTrainingGroup() {
    doNothing().when(this.groupRepo).delete(any(TrainingGroup.class));
    Set<User> players = new HashSet<>(List.of(new User()));
    TrainingGroup group = new TrainingGroup();
    group.setPlayers(players);
    this.groupService.deleteGroup(group);
    Assertions.assertEquals(0, group.getPlayers().size(), "Players were not deleted");
  }

  @Test
  public void testCalcAttendance() {
    TrainingGroup group = new TrainingGroup();
    User p1 = User.builder().username("player1").build();
    User p2 = User.builder().username("player2").build();
    group.setPlayers(new HashSet<>(List.of(p1, p2)));
    Training t1 = Training.builder().attendees(new HashSet<>(List.of(p1, p2))).build();
    Training t2 = Training.builder().attendees(new HashSet<>(List.of(p2))).build();
    Training t3 = Training.builder().attendees(new HashSet<>(List.of(p2))).build();
    group.setTrainings(new HashSet<>(List.of(t1, t2, t3)));
    Map<String, Integer> attendance = this.groupService.calcAttendance(group);
    Assertions.assertEquals(1, attendance.get("player1"), "Wrong attendance");
    Assertions.assertEquals(3, attendance.get("player2"), "Wrong attendance");
  }

  @Test
  public void testGetNumOfGroups() {
    Club c1 = new Club();
    c1.setName("Club1");
    Club c2 = new Club();
    c2.setName("Club2");
    TrainingGroup g1 = new TrainingGroup();
    TrainingGroup g2 = new TrainingGroup();
    TrainingGroup g3 = new TrainingGroup();
    doReturn(new HashSet<>(List.of(g1))).when(this.groupRepo)
      .findByClub_NameContaining(c1.getName());
    doReturn(new HashSet<>(List.of(g2, g3))).when(this.groupRepo)
      .findByClub_NameContaining(c2.getName());

/*
    Map<String, Integer> numGroupsByClub = this.groupService.getNumOfGroups(List.of(c1, c2));
    Assertions.assertEquals(1, numGroupsByClub.get(c1.getName()), "Wrong group-num");
    Assertions.assertEquals(2, numGroupsByClub.get(c2.getName()), "Wrong group-num");
    */
  }

}