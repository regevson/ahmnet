package at.qe.skeleton.tests;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import at.qe.skeleton.model.TrainingGroup;
import at.qe.skeleton.model.User;
import at.qe.skeleton.services.TrainingGroupService;
import at.qe.skeleton.services.UserService;

@SpringBootTest
@WebAppConfiguration
public class TrainingGroupServiceTest {

    @Autowired
    TrainingGroupService trainingGroupService;
    @Autowired
    UserService userService;

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetGroupsByClub() {
        Set<TrainingGroup> groups = this.trainingGroupService.loadTrainingGroupsByClub("TC Vomp");
        Assertions.assertTrue(groups.size() > 1, "Wrong amount of groups");
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetGroupsByTrainer() {
        User trainer = userService.loadUser("johndoe");
        Assertions.assertNotNull(trainer, "User \"" + trainer + "\" could not be loaded from test data source");
        Set<TrainingGroup> groups = this.trainingGroupService.loadTrainingGroupByTrainer(trainer);
        Assertions.assertEquals(2, groups.size(), "Wrong amount of groups");
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetGroupsByPlayer() {
        User player = userService.loadUser("user1");
        Assertions.assertNotNull(player, "User \"" + player + "\" could not be loaded from test data source");
        Set<TrainingGroup> groups = this.trainingGroupService.loadTrainingGroupByPlayer(player);
        for(TrainingGroup g : groups)
            Assertions.assertTrue(g.getPlayers().contains(player), "There is a group that doesn't have player " + player);
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetGroupsById() {
        User trainer = userService.loadUser("johndoe");
        Assertions.assertNotNull(trainer, "User \"" + trainer + "\" could not be loaded from test data source");
        TrainingGroup group = this.trainingGroupService.loadTrainingGroupById(1);
        Assertions.assertNotNull(group, "Group \"" + group + "\" could not be loaded from test data source");
        Assertions.assertEquals(trainer, group.getTrainer(), "Wrong trainer");
    }



}