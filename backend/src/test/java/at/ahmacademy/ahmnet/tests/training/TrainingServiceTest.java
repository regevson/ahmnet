package at.ahmacademy.ahmnet.tests.training;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.model.TrainingGroup;
import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.repositories.TrainingRepository;
import at.ahmacademy.ahmnet.services.training.TrainingAuthService;
import at.ahmacademy.ahmnet.services.training.TrainingService;
import at.ahmacademy.ahmnet.services.user.UserAuthService;
import at.ahmacademy.ahmnet.services.user.UserService;

@SpringBootTest
@WebAppConfiguration
public class TrainingServiceTest {

  @Mock
  TrainingRepository trainingRepo;

  @Mock
  UserService userService;
  @Mock
  TrainingAuthService trgAuth;
  @Mock
  UserAuthService usrAuth;

  @InjectMocks
  TrainingService trainingService;


  private void disableAuth() {
    doReturn(true).when(usrAuth).authUsrIsAdmin();
    doReturn(true).when(usrAuth).authUsrIsTrainer();
    doReturn(true).when(trgAuth).hasTrainer(any(Training.class), anyString());
    doReturn(true).when(trgAuth).isFree(any(Training.class));
  }

  @Test
  public void testLoadFreeTrainings() {
    // test with excluding 'admin'
    List<List<Training>> groupedList = trainingService.loadFreeTrainings(8, Optional.of("admin"));
    Assertions.assertTrue(groupedList.stream().allMatch(dayList -> dayList.stream()
                                              .allMatch(t -> t.getIsFree() 
                                                             && t.getWeekNum() == 8
                                                             && !trgAuth.hasTrainer(t, "admin"))));

    // test without exclusion
    groupedList = trainingService.loadFreeTrainings(8, Optional.empty());
    Assertions.assertTrue(groupedList.stream().allMatch(dayList -> dayList.stream()
                                              .allMatch(t -> t.getIsFree() && t.getWeekNum() == 8)));
    Assertions.assertTrue(groupedList.stream().allMatch(dayList -> dayList.isEmpty() || dayList.stream()
                                              .anyMatch(t -> trgAuth.hasTrainer(t, "admin"))));
    Assertions.assertTrue(groupedList.stream().allMatch(dayList -> dayList.isEmpty() || dayList.stream()
                                              .anyMatch(t -> trgAuth.hasTrainer(t, "johndoe"))));
  }

  @Test
  public void testLoadTrainingsByTrainer() {
    // test with free-flag to true
    List<List<Training>> groupedList 
      = trainingService.loadTrainingsByTrainer("johndoe", 8, Optional.of(true));
    Assertions.assertTrue(groupedList.stream().allMatch(dayList -> dayList.stream()
                                              .allMatch(t -> t.getIsFree() 
                                                             && t.getWeekNum() == 8 
                                                             && trgAuth.hasTrainer(t, "johndoe"))));
                                                             

    // test with free-flag to false
    groupedList = trainingService.loadTrainingsByTrainer("johndoe", 8, Optional.of(false));
    Assertions.assertTrue(groupedList.stream().allMatch(dayList -> dayList.stream()
                                              .allMatch(t -> !t.getIsFree() 
                                                             && t.getWeekNum() == 8
                                                             && trgAuth.hasTrainer(t, "johndoe"))));
                                                             

    // test without free-flag
    groupedList = trainingService.loadTrainingsByTrainer("johndoe", 8, Optional.empty());
    Assertions.assertTrue(groupedList.stream().allMatch(dayList -> dayList.isEmpty() || dayList.stream()
                                              .anyMatch(t -> t.getIsFree())));
    Assertions.assertTrue(groupedList.stream().allMatch(dayList -> dayList.isEmpty() || dayList.stream()
                                              .anyMatch(t -> !t.getIsFree())));

  }

  @Test
  public void testDeleteTraining() {
    doNothing().when(trainingRepo).delete(any(Training.class));
    Training tr = new Training();
    Set<Training> trainings = new HashSet<>(List.of(tr));
    TrainingGroup gr = new TrainingGroup();
    gr.setTrainings(trainings);
    tr.setTrainingGroup(gr);
    Set<User> attendees = new HashSet<>(List.of(new User(), new User()));
    tr.setAttendees(attendees);
    trainingService.deleteTraining(tr);
    Assertions.assertEquals(0, tr.getAttendees().size(), "Attendees were not deleted");
    Assertions.assertFalse(tr.getTrainingGroup().getTrainings().contains(tr), "Training was not deleted from group");
  }

  @Disabled
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
    /*
    List<List<Training>> groupedList = trainingService.groupByDay(trs);
    Assertions.assertTrue(groupedList.get(time1.getDayOfWeek().getValue() - 1).contains(t1), "First training was wrongly grouped");
    Assertions.assertTrue(groupedList.get(time2.getDayOfWeek().getValue() - 1).contains(t2), "Second training was wrongly grouped");
    Assertions.assertTrue(groupedList.get(time3.getDayOfWeek().getValue() - 1).contains(t3), "Third training was wrongly grouped");
    */
  }

  @Test
  public void testGetDatesInWeek() {
    List<String> dates = trainingService.getDatesInWeek(8);
    Assertions.assertEquals("2022-02-21", dates.get(0), "Date is wrong");
    Assertions.assertEquals("2022-02-22", dates.get(1), "Date is wrong");
    Assertions.assertEquals("2022-02-23", dates.get(2), "Date is wrong");
    Assertions.assertEquals("2022-02-24", dates.get(3), "Date is wrong");
    Assertions.assertEquals("2022-02-25", dates.get(4), "Date is wrong");
    Assertions.assertEquals("2022-02-26", dates.get(5), "Date is wrong");
    Assertions.assertEquals("2022-02-27", dates.get(6), "Date is wrong");
  }

  @Test
  public void testFreeTraining() {
    disableAuth();
    doReturn(null).when(trainingRepo).save(any(Training.class));
    Training t = Training.builder().isFree(false).build();
    User freeingTrainer = User.builder().username("freeingTrainer").build();
    t.setTrainer(freeingTrainer);
    User prevTrainer = User.builder().username("prevTrainer").build();
    t.setPrevTrainer(prevTrainer);
    trainingService.freeTrainings(List.of(t), false);
    Assertions.assertEquals("prevTrainer", t.getTrainer().getId(), "Wrong prev trainer");
    Assertions.assertEquals("prevTrainer", t.getPrevTrainer().getId(), "Wrong prev trainer");
    Assertions.assertTrue(t.getIsFree(), "Training was not freed");
  }

  @Test
  public void testGrabTraining() {
    disableAuth();
    doReturn(null).when(trainingRepo).save(any(Training.class));
    User loggedInUser = User.builder().username("loggedInUser").build();
    doReturn(loggedInUser).when(userService).getAuthUser();
    Training t = Training.builder().isFree(true).build();
    User prevTrainer = User.builder().username("prevTrainer").build();
    t.setTrainer(prevTrainer);
    trainingService.grabTrainings(List.of(t), false);
    Assertions.assertEquals("loggedInUser", t.getTrainer().getId(), "Wrong new trainer");
    Assertions.assertEquals("prevTrainer", t.getPrevTrainer().getId(), "Wrong prev trainer");
    Assertions.assertFalse(t.getIsFree(), "Training was not grabbed");
  }

  @Test
  public void testSaveRecurringTrainings() {
    doReturn(null).when(trainingRepo).save(any(Training.class));
    LocalDateTime now = LocalDateTime.now();
    Training tr = Training.builder().dateTime(now).lastDate(now.plusWeeks(2).toLocalDate()).build();
    List<Training> recTrs = trainingService.saveNewTraining(tr);
    Assertions.assertEquals(3, recTrs.size(), "Wrong recurring-training-size");
    Assertions.assertEquals(now, recTrs.get(0).getDateTime(), "Wrong prev trainer;");
    Assertions.assertEquals(now.plusWeeks(1), recTrs.get(1).getDateTime(), "Wrong recurring Training");
    Assertions.assertEquals(now.plusWeeks(2), recTrs.get(2).getDateTime(), "Wrong recurring Training");
  }

}