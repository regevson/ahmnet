package at.ahmacademy.ahmnet.tests.club;

import static at.ahmacademy.ahmnet.model.UserRole.PLAYER;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;

import at.ahmacademy.ahmnet.model.Club;
import at.ahmacademy.ahmnet.model.TrainingGroup;
import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.services.club.ClubService;
import at.ahmacademy.ahmnet.services.trainingGroup.TrainingGroupService;
import at.ahmacademy.ahmnet.services.user.UserService;

@SpringBootTest
@WebAppConfiguration
public class ClubServiceTest {

  @Autowired
  ClubService clubService;

  @Test
  @WithMockUser(username = "admin", authorities = {"ADMIN"})
  public void testGetNumOfPlayersByClubs() {
    UserService fakeUserSer = mock(UserService.class); 
    clubService.setUserService(fakeUserSer);

    Club c1 = Club.builder().id("TC-Wiesing").build();
    Club c2 = Club.builder().id("TC-Schwaz").build();

    User p1 = User.builder().username("p1").club(c1).build();
    User p2 = User.builder().username("p2").club(c1).build();
    User p3 = User.builder().username("p3").club(c2).build();

    doReturn(Set.of(p1,p2)).when(fakeUserSer).loadUsersByClubAndRole(c1.getId(), Optional.of(PLAYER));
    doReturn(Set.of(p3)).when(fakeUserSer).loadUsersByClubAndRole(c2.getId(), Optional.of(PLAYER));

    Map<String, Integer> players = clubService.getNumOfPlayers(Set.of(c1,c2));
    Assertions.assertEquals(2, players.get(c1.getId()), "wrong num of players");
    Assertions.assertEquals(1, players.get(c2.getId()), "wrong num of players");
  }

  @Test
  @WithMockUser(username = "admin", authorities = {"ADMIN"})
  public void testGetNumOfGroupsByClubs() {
    TrainingGroupService fakeGroupSer = mock(TrainingGroupService.class); 
    clubService.setGroupService(fakeGroupSer);

    Club c1 = Club.builder().id("TC-Wiesing").build();
    Club c2 = Club.builder().id("TC-Schwaz").build();

    TrainingGroup g1 = TrainingGroup.builder().id(1L).club(c1).build();
    TrainingGroup g2 = TrainingGroup.builder().id(2L).club(c1).build();
    TrainingGroup g3 = TrainingGroup.builder().id(3L).club(c2).build();

    doReturn(Set.of(g1,g2)).when(fakeGroupSer).loadGroupsByClub(c1.getId());
    doReturn(Set.of(g3)).when(fakeGroupSer).loadGroupsByClub(c2.getId());

    Map<String, Integer> groups = clubService.getNumOfGroups(Set.of(c1,c2));
    Assertions.assertEquals(2, groups.get(c1.getId()), "wrong num of groups");
    Assertions.assertEquals(1, groups.get(c2.getId()), "wrong num of groups");
  }

}