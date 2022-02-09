package at.qe.skeleton.tests;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import at.qe.skeleton.model.Training;
import at.qe.skeleton.model.TrainingGroup;
import at.qe.skeleton.model.User;
import at.qe.skeleton.services.TrainingGroupService;
import at.qe.skeleton.services.TrainingService;
import at.qe.skeleton.services.UserService;

@SpringBootTest
@WebAppConfiguration
public class TrainingServiceTest {


    @Autowired
    TrainingService trainingService;
    @Autowired
    TrainingGroupService trainingGroupService;
    @Autowired
    UserService userService;

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetTrainingsByTrainingGroup() {
        TrainingGroup group = trainingGroupService.loadTrainingGroupById(0);
        Assertions.assertNotNull(group, "Group \"" + group + "\" could not be loaded from test data source");

        List<Training> trainings = trainingService.orderTrainingsAsc(group.getTrainings());
        Assertions.assertEquals(1, trainings.get(0).getId(), "Wrong training! " + trainings.get(0));
        Assertions.assertEquals(0, trainings.get(1).getId(), "Wrong training! " + trainings.get(1));
        Assertions.assertTrue(trainings.size() > 1, "Wrong training-size!");
        Assertions.assertTrue(trainings.get(0).getDateTime().isBefore(trainings.get(1).getDateTime()), "Trainings are wrongly sorted!");
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetTrainingsByPlayer() {
        User user = userService.loadUser("susi");
        Assertions.assertNotNull(user, "User \"" + user + "\" could not be loaded from test data source");

        List<Training> trainings = this.trainingService.loadTrainingsByPlayer(user);
        Assertions.assertTrue(trainings.size() > 1, "Wrong training-size!");
        Assertions.assertTrue(trainings.get(0).getDateTime().isBefore(trainings.get(1).getDateTime()), "Trainings are wrongly sorted!");
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetTrainingsByTrainer() {
        User user = userService.loadUser("johndoe");
        Assertions.assertNotNull(user, "User \"" + user + "\" could not be loaded from test data source");

        List<Training> trainings = this.trainingService.loadTrainingsByTrainer(user);
        Assertions.assertTrue(trainings.size() > 1, "Wrong training-size!");
        Assertions.assertTrue(trainings.get(0).getDateTime().isBefore(trainings.get(1).getDateTime()), "Trainings are wrongly sorted!");
        Assertions.assertTrue(trainings.get(1).getDateTime().isBefore(trainings.get(2).getDateTime()), "Trainings are wrongly sorted!");
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetTrainingsByPlayerAndWeek() {
	int weekNum = 6;
        User user = userService.loadUser("susi");
        Assertions.assertNotNull(user, "User \"" + user + "\" could not be loaded from test data source");

        List<Training> trainings = this.trainingService.loadTrainingsByPlayerAndWeek(user, weekNum);
        Assertions.assertTrue(trainings.size() > 1, "Wrong training-size!");
        Assertions.assertTrue(trainings.get(0).getDateTime().isBefore(trainings.get(1).getDateTime()), "Trainings are wrongly sorted!");
        Assertions.assertEquals(weekNum, trainings.get(0).getWeekNum(), "Wrong weekNum!");
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetTrainingsByTrainerAndWeek() {
	int weekNum = 6;
        User user = userService.loadUser("johndoe");
        Assertions.assertNotNull(user, "User \"" + user + "\" could not be loaded from test data source");

        List<Training> trainings = this.trainingService.loadTrainingsByTrainerAndWeek(user.getId(), weekNum);
        Assertions.assertTrue(trainings.size() > 1, "Wrong training-size!");
        Assertions.assertTrue(trainings.get(0).getDateTime().isBefore(trainings.get(1).getDateTime()), "Trainings are wrongly sorted!");
        Assertions.assertTrue(trainings.get(1).getDateTime().isBefore(trainings.get(2).getDateTime()), "Trainings are wrongly sorted!");
        Assertions.assertEquals(weekNum, trainings.get(0).getWeekNum(), "Wrong weekNum!");
    }


    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetTrainingById() {
        User user = userService.loadUser("johndoe");
        Assertions.assertNotNull(user, "User \"" + user + "\" could not be loaded from test data source");

        Training training = this.trainingService.loadTrainingById(1);
        Assertions.assertEquals(1, training.getId(), "wrong training");
    }

}
