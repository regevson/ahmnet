package at.ahmacademy.ahmnet.tests.club;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
public class ClubServiceTest {

/*
  @Mock
  TrainingRepository trainingRepo;

  @Mock
  UserService fakeUserService;
  @Mock
  TrainingAuthService fakeTrgAuth;
  @Mock
  UserAuthService fakeUsrAuth;

  @InjectMocks
  TrainingService fakeTrainingService;


  private void disableAuth() {
    doReturn(true).when(fakeUsrAuth).authUsrIsAdmin();
    doReturn(true).when(fakeUsrAuth).authUsrIsTrainer();
    doReturn(true).when(fakeTrgAuth).hasTrainer(any(Training.class), anyString());
    doReturn(true).when(fakeTrgAuth).isFree(any(Training.class));
  }

  @Autowired
  TrainingService trainingService;
  @Autowired
  TrainingAuthService trgAuth;

  @Test
  @WithMockUser(username = "admin", authorities = {"ADMIN"})
  public void testLoadFreeTrainings() {
    // test with excluding 'mare'
    int weekNum = 27;
    List<List<Training>> groupedList = trainingService.loadFreeTrainings(weekNum, Optional.of("mare"));
    Assertions.assertTrue(groupedList.stream().allMatch(dayList -> dayList.stream()
                                              .allMatch(t -> t.getIsFree() 
                                                             && t.getWeekNum() == weekNum
                                                             && !trgAuth.hasTrainer(t, "mare"))));

    // test without exclusion
    groupedList = trainingService.loadFreeTrainings(weekNum, Optional.empty());
    Assertions.assertTrue(groupedList.stream().allMatch(dayList -> dayList.stream()
                                              .allMatch(t -> t.getIsFree() && t.getWeekNum() == weekNum)));
    Assertions.assertTrue(groupedList.stream().allMatch(dayList -> dayList.isEmpty() || dayList.stream()
                                              .allMatch(t -> trgAuth.hasTrainer(t, "admin") ||
                                                             trgAuth.hasTrainer(t, "jure") ||
                                                             trgAuth.hasTrainer(t, "mare"))));
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
                                                             && trgAuth.hasTrainer(t, "jure"))));
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
                                                             && trgAuth.hasTrainer(t, "jure"))));
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
    fakeTrainingService.freeTrainings(List.of(t), false);
    Assertions.assertEquals("prevTrainer", t.getTrainer().getId(), "Wrong prev trainer");
    Assertions.assertEquals("prevTrainer", t.getPrevTrainer().getId(), "Wrong prev trainer");
    Assertions.assertTrue(t.getIsFree(), "Training was not freed");
  }

  @Test
  public void testGrabTraining() {
    disableAuth();
    doReturn(null).when(trainingRepo).save(any(Training.class));
    User loggedInUser = User.builder().username("loggedInUser").build();
    doReturn(loggedInUser).when(fakeUserService).getAuthUser();
    Training t = Training.builder().isFree(true).build();
    User prevTrainer = User.builder().username("prevTrainer").build();
    t.setTrainer(prevTrainer);
    fakeTrainingService.grabTrainings(List.of(t), false);
    Assertions.assertEquals("loggedInUser", t.getTrainer().getId(), "Wrong new trainer");
    Assertions.assertEquals("prevTrainer", t.getPrevTrainer().getId(), "Wrong prev trainer");
    Assertions.assertFalse(t.getIsFree(), "Training was not grabbed");
  }

  @Test
  public void testSaveRecurringTrainings() {
    doReturn(null).when(trainingRepo).save(any(Training.class));
    LocalDateTime now = LocalDateTime.now();
    Training tr = Training.builder().dateTime(now).lastDate(now.plusWeeks(2).toLocalDate()).build();
    List<Training> recTrs = fakeTrainingService.saveNewTraining(tr);
    Assertions.assertEquals(3, recTrs.size(), "Wrong recurring-training-size");
    Assertions.assertEquals(now, recTrs.get(0).getDateTime(), "Wrong prev trainer;");
    Assertions.assertEquals(now.plusWeeks(1), recTrs.get(1).getDateTime(), "Wrong recurring Training");
    Assertions.assertEquals(now.plusWeeks(2), recTrs.get(2).getDateTime(), "Wrong recurring Training");
  }
  */

}