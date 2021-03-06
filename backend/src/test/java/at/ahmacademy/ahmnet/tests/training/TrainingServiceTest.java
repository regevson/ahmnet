package at.ahmacademy.ahmnet.tests.training;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.repositories.TrainingRepository;
import at.ahmacademy.ahmnet.repositories.UserRepository;
import at.ahmacademy.ahmnet.services.training.TrainingService;
import at.ahmacademy.ahmnet.services.user.UserService;

@SpringBootTest
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class TrainingServiceTest {

  @Autowired
  TrainingRepository trainingRepository;
  @Autowired
  UserRepository userRepository;

  TrainingService trainingService;
  UserService userService;
  @BeforeEach
  public void initTrainingService() {
    trainingService = new TrainingService();
    trainingService.setTrainingRepo(trainingRepository);
    userService = new UserService();
    userService.setUserRepository(userRepository);
    trainingService.setUserService(userService);
  }

  @Test
  @WithMockUser(username = "admin", authorities = {"ADMIN"})
  public void testLoadFreeTrainings() {
    // test with excluding 'mare'
    int weekNum = 27;
    List<List<Training>> groupedList = trainingService.loadFreeTrainings(weekNum, Optional.of("mare"));
    Assertions.assertTrue(groupedList.stream().allMatch(dayList -> dayList.stream()
                                              .allMatch(t -> t.getIsFree() 
                                                             && t.getWeekNum() == weekNum
                                                             && !trainingService.hasTrainer(t, "mare"))));

    // test without exclusion
    groupedList = trainingService.loadFreeTrainings(weekNum, Optional.empty());
    Assertions.assertTrue(groupedList.stream().allMatch(dayList -> dayList.stream()
                                              .allMatch(t -> t.getIsFree() && t.getWeekNum() == weekNum)));
    Assertions.assertTrue(groupedList.stream().allMatch(dayList -> dayList.isEmpty() || dayList.stream()
                                              .allMatch(t -> trainingService.hasTrainer(t, "admin") ||
                                                             trainingService.hasTrainer(t, "jure") ||
                                                             trainingService.hasTrainer(t, "mare"))));
  }

  @Test
  @WithMockUser(username = "admin", authorities = {"ADMIN"})
  public void testLoadTrainingsByTrainer_with_free_flag_is_true() {
    int weekNum = 27;
    List<List<Training>> groupedList =
        trainingService.loadTrainingsByTrainer("jure", weekNum, Optional.of(true));
    Assertions.assertTrue(groupedList.stream().allMatch(dayList -> dayList.stream()
                                              .allMatch(t -> t.getIsFree() 
                                                             && t.getWeekNum() == weekNum 
                                                             && trainingService.hasTrainer(t, "jure"))));
  }

  @Test
  @WithMockUser(username = "admin", authorities = {"ADMIN"})
  public void testLoadTrainingsByTrainer_with_free_flag_is_false() {
    int weekNum = 27;
    List<List<Training>> groupedList = 
        trainingService.loadTrainingsByTrainer("jure", weekNum, Optional.of(false));
    Assertions.assertTrue(groupedList.stream().allMatch(dayList -> dayList.stream()
                                              .allMatch(t -> !t.getIsFree() 
                                                             && t.getWeekNum() == weekNum
                                                             && trainingService.hasTrainer(t, "jure"))));
  }
  
  @Test
  @WithMockUser(username = "admin", authorities = {"ADMIN"})
  public void testLoadTrainingsByTrainer_without_free_flag() {
    int weekNum = 27;
    List<List<Training>> groupedList = 
        trainingService.loadTrainingsByTrainer("jure", weekNum, Optional.empty());
    Assertions.assertTrue(groupedList.stream().allMatch(dayList -> dayList.isEmpty() || dayList.stream()
                                              .anyMatch(t -> t.getIsFree())));
    Assertions.assertTrue(groupedList.stream().allMatch(dayList -> dayList.isEmpty() || dayList.stream()
                                              .anyMatch(t -> !t.getIsFree())));

  }

  @Transactional
  @Test
  @DirtiesContext
  @WithMockUser(username = "admin", authorities = {"ADMIN"})
  public void testDeleteTraining() {
    Long id = 6L;
    Training t = trainingService.loadTrainingById(id);
    trainingService.deleteTraining(t);
    Assertions.assertNull(trainingService.loadTrainingById(id));
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
    TrainingRepository fakeTrRepo = Mockito.mock(TrainingRepository.class);
    doReturn(null).when(fakeTrRepo).save(any(Training.class));

    TrainingService fakeTrService = new TrainingService();
    fakeTrService.setTrainingRepo(fakeTrRepo);
    UserService fakeUserService = Mockito.mock(UserService.class);
    doReturn(true).when(fakeUserService).isAdmin();
    fakeTrService.setUserService(fakeUserService);

    Training t = Training.builder().isFree(false).build();
    User freeingTrainer = User.builder().username("freeingTrainer").build();
    t.setTrainer(freeingTrainer);
    User prevTrainer = User.builder().username("prevTrainer").build();
    t.setPrevTrainer(prevTrainer);

    fakeTrService.freeTrainings(List.of(t), false);
    Assertions.assertEquals("prevTrainer", t.getTrainer().getId(), "Wrong prev trainer");
    Assertions.assertEquals("prevTrainer", t.getPrevTrainer().getId(), "Wrong prev trainer");
    Assertions.assertTrue(t.getIsFree(), "Training was not freed");
  }

  @Test
  public void testGrabTraining() {
    TrainingRepository fakeTrRepo = Mockito.mock(TrainingRepository.class);
    doReturn(null).when(fakeTrRepo).save(any(Training.class));

    UserService fakeUserService = Mockito.mock(UserService.class);
    doReturn(true).when(fakeUserService).isAdmin();

    TrainingService fakeTrService = new TrainingService();
    fakeTrService.setTrainingRepo(fakeTrRepo);
    fakeTrService.setUserService(fakeUserService);


    User loggedInUser = User.builder().username("loggedInUser").build();
    doReturn(loggedInUser).when(fakeUserService).getAuthUser();
    Training t = Training.builder().isFree(true).build();
    User prevTrainer = User.builder().username("prevTrainer").build();
    t.setTrainer(prevTrainer);

    fakeTrService.grabTrainings(List.of(t), false);
    Assertions.assertEquals("loggedInUser", t.getTrainer().getId(), "Wrong new trainer");
    Assertions.assertEquals("prevTrainer", t.getPrevTrainer().getId(), "Wrong prev trainer");
    Assertions.assertFalse(t.getIsFree(), "Training was not grabbed");
  }

  @Test
  public void testSaveRecurringTrainings() {
    TrainingRepository fakeTrRepo = Mockito.mock(TrainingRepository.class);
    doReturn(null).when(fakeTrRepo).save(any(Training.class));

    TrainingService fakeTrService = new TrainingService();
    fakeTrService.setTrainingRepo(fakeTrRepo);

    LocalDateTime now = LocalDateTime.now();
    Training tr = Training.builder().dateTime(now).lastDate(now.plusWeeks(2).toLocalDate()).build();

    List<Training> recTrs = fakeTrService.saveNewTraining(tr);
    Assertions.assertEquals(3, recTrs.size(), "Wrong recurring-training-size");
    Assertions.assertEquals(now, recTrs.get(0).getDateTime(), "Wrong prev trainer;");
    Assertions.assertEquals(now.plusWeeks(1), recTrs.get(1).getDateTime(), "Wrong recurring Training");
    Assertions.assertEquals(now.plusWeeks(2), recTrs.get(2).getDateTime(), "Wrong recurring Training");
  }

}