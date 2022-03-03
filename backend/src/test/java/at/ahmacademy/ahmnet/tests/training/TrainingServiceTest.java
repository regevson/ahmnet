package at.ahmacademy.ahmnet.tests.training;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.model.TrainingGroup;
import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.repositories.TrainingRepository;
import at.ahmacademy.ahmnet.services.training.TrainingService;
import at.ahmacademy.ahmnet.services.user.UserService;

@SpringBootTest
@WebAppConfiguration
public class TrainingServiceTest {

    @Mock
    TrainingRepository trainingRepo;

    @Mock
    UserService userService;

    @InjectMocks
    TrainingService trainingService;


/*
    @Test
    public void testDeleteTraining() {
	doNothing().when(this.trainingRepo).delete(any(Training.class));
	Training tr = new Training();
	Set<Training> trainings = new HashSet<>(Arrays.asList(tr));
	TrainingGroup gr = new TrainingGroup();
	gr.setTrainings(trainings);
	tr.setTrainingGroup(gr);
	Set<User> attendees = new HashSet<>(Arrays.asList(new User(), new User()));
	tr.setAttendees(attendees);
	this.trainingService.deleteTraining(tr);
        Assertions.assertEquals(0, tr.getAttendees().size(), "Attendees were not deleted");
        Assertions.assertFalse(tr.getTrainingGroup().getTrainings().contains(tr), "Training was not deleted from group");
    }
    */
    
    @Test
    public void testGroupByDay() {
	Training t1 = new Training();
	LocalDateTime time1 = LocalDateTime.now();
	t1.setDateTime(time1);
	Training t2 = new Training();
	LocalDateTime time2 = LocalDateTime.now().plusMinutes(1);
	t2.setDateTime(time2);
	Training t3 = new Training();
	LocalDateTime time3 = LocalDateTime.now().plusDays(2);
	t3.setDateTime(time3);
	List<Training> trs = new ArrayList<>(List.of(t1, t2, t3));
	List<List<Training>> groupedList = this.trainingService.groupByDay(trs);
        Assertions.assertTrue(groupedList.get(time1.getDayOfWeek().getValue()-1).contains(t1), "First training was wrongly grouped");
        Assertions.assertTrue(groupedList.get(time2.getDayOfWeek().getValue()-1).contains(t2), "Second training was wrongly grouped");
        Assertions.assertTrue(groupedList.get(time3.getDayOfWeek().getValue()-1).contains(t3), "Third training was wrongly grouped");
    }

    @Test
    public void testGetDatesInWeek() {
	List<String> dates = this.trainingService.getDatesInWeek(8);
        Assertions.assertEquals("2022-02-21", dates.get(0), "Date is wrong");
        Assertions.assertEquals("2022-02-22", dates.get(1), "Date is wrong");
        Assertions.assertEquals("2022-02-23", dates.get(2), "Date is wrong");
        Assertions.assertEquals("2022-02-24", dates.get(3), "Date is wrong");
        Assertions.assertEquals("2022-02-25", dates.get(4), "Date is wrong");
        Assertions.assertEquals("2022-02-26", dates.get(5), "Date is wrong");
        Assertions.assertEquals("2022-02-27", dates.get(6), "Date is wrong");
    }
    
    /*
    @Test
    public void testFreeTraining() {
	doReturn(null).when(this.trainingRepo).save(any(Training.class));
	Training t = new Training();
	t.setIsFree(false);
	User freeingTrainer = new User();
	freeingTrainer.setId("freeingTrainer");
	t.setTrainer(freeingTrainer);
	User prevTrainer = new User();
	prevTrainer.setId("prevTrainer");
	t.setPrevTrainer(prevTrainer);
	this.trainingService.freeTraining(t);
        Assertions.assertEquals("prevTrainer", t.getTrainer().getId(), "Wrong prev trainer");
        Assertions.assertEquals("prevTrainer", t.getPrevTrainer().getId(), "Wrong prev trainer");
        Assertions.assertTrue(t.getIsFree(), "Training was not freed");
    }
    */
    
    /*
    @Test
    public void testGrabTraining() {
	doReturn(null).when(this.trainingRepo).save(any(Training.class));
	User loggedInUser = new User();
	loggedInUser.setId("loggedInUser");
	doReturn(loggedInUser).when(this.userService).getAuthenticatedUser();
	Training t = new Training();
	t.setIsFree(true);
	User prevTrainer = new User();
	prevTrainer.setId("prevTrainer");
	t.setTrainer(prevTrainer);
	this.trainingService.grabTraining(t);
        Assertions.assertEquals("loggedInUser", t.getTrainer().getId(), "Wrong new trainer");
        Assertions.assertEquals("prevTrainer", t.getPrevTrainer().getId(), "Wrong prev trainer");
        Assertions.assertFalse(t.getIsFree(), "Training was not grabbed");
    }
    */
    
    /*
    @Test
    public void testSaveRecurringTrainings() {
	doReturn(null).when(this.trainingRepo).save(any(Training.class));
	Training tr = new Training();
	LocalDateTime now = LocalDateTime.now();
	tr.setDateTime(now);
	tr.setLastDate(now.plusWeeks(2).toLocalDate());
	List<Training> recTrs = this.trainingService.saveRecurringTrainings(tr);
        Assertions.assertEquals(3, recTrs.size(), "Wrong recurring-training-size");
        Assertions.assertEquals(now, recTrs.get(0).getDateTime(), "Wrong prev trainer;");
        Assertions.assertEquals(now.plusWeeks(1), recTrs.get(1).getDateTime(), "Wrong recurring Training");
        Assertions.assertEquals(now.plusWeeks(2), recTrs.get(2).getDateTime(), "Wrong recurring Training");
    }
    */

}
