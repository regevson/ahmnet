package at.ahmacademy.ahmnet.dtos;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.services.training.TrainingService;
import at.ahmacademy.ahmnet.services.trainingGroup.ClubService;
import at.ahmacademy.ahmnet.services.trainingGroup.TrainingGroupService;
import at.ahmacademy.ahmnet.services.user.UserService;

@Component
public class TrainingMapper {

  @Autowired
  TrainingService trainingService;
  @Autowired
  UserService userService;
  @Autowired
  TrainingGroupMapper groupMapper;
  @Autowired
  ClubMapper clubMapper;
  @Autowired
  TrainingGroupService groupService;
  @Autowired
  ClubService clubService;
  @Autowired
  UserMapper userMapper;

  public TrainingSnippetDto mapToTrainingSnippetDto(Training tr) {
    TrainingSnippetDto dto = new TrainingSnippetDto();
    dto.setId(tr.getId());
    dto.setGroupId(tr.getTrainingGroup().getId());
    dto.setClubId(tr.getClub().getId());
    dto.setGroupClubId(tr.getTrainingGroup().getClub().getId());
    dto.setDate(tr.getDateTime().toLocalDate().toString());
    LocalTime startTime = tr.getDateTime().toLocalTime();
    dto.setTimeslot(startTime.toString() + " - " + startTime.plusMinutes(tr.getDurationMinutes()).toString());
    dto.setStartTime(startTime);
    dto.setCourt(tr.getCourt());
    dto.setFree(tr.getIsFree());
    dto.setPrevTrainerId(tr.getPrevTrainer().getId());
    dto.setTrainerId(tr.getTrainer().getId());
    return dto;
  }

  public List<TrainingSnippetDto> mapToTrainingSnippetDto(Iterable<Training> trainings) {
    List<TrainingSnippetDto> dtos = new ArrayList<>();
    for(Training t: trainings)
      dtos.add(mapToTrainingSnippetDto(t));
    return dtos;
  }


  public TrainingDto mapToTrainingDto(Training tr) {
    TrainingDto dto = new TrainingDto();
    dto.setId(tr.getId());
    dto.setDate(tr.getDateTime().toLocalDate());
    dto.setLastDate(tr.getLastDate());
    LocalTime startTime = tr.getDateTime().toLocalTime();
    dto.setStartTime(startTime);
    dto.setTimeslot(startTime.toString() + " - " + startTime.plusMinutes(tr.getDurationMinutes()).toString());
    dto.setWeekNum(tr.getWeekNum());
    dto.setDurationMinutes(tr.getDurationMinutes());
    dto.setCourt(tr.getCourt());
    dto.setBulletPoints(tr.getBulletPoints());
    dto.setComment(tr.getComment());
    dto.setFree(tr.getIsFree());
    dto.setPlayerIds(tr.getTrainingGroup().getPlayers().stream().map(u -> u.getId()).collect(Collectors.toSet()));
    dto.setAttendeeIds(tr.getAttendees().stream().map(u -> u.getId()).collect(Collectors.toList()));
    dto.setGroupId(tr.getTrainingGroup().getId());
    dto.setTrainerId(tr.getTrainer().getId());
    dto.setPrevTrainerId(tr.getPrevTrainer().getId());
    dto.setClubId(tr.getClub().getId());

    String playerClubIds = tr.getTrainingGroup().getPlayers().stream().map(a -> a.getClub().getId()).collect(Collectors.joining(","));
    dto.setAttendees_url("clubs/" + playerClubIds + "/players/" + dto.getPlayerIds());
    String attendeeClubIds = tr.getAttendees().stream().map(a -> a.getClub().getId()).collect(Collectors.joining(","));
    dto.setAttendees_url("clubs/" + attendeeClubIds + "/players/" + dto.getAttendeeIds());
    dto.setGroup_url("clubs/" + tr.getTrainingGroup().getClub().getId() + "/groups/" + dto.getGroupId());
    dto.setTrainer_url("trainer/" + dto.getTrainerId());
    dto.setPrevTrainer_url("trainer/" + dto.getPrevTrainerId());
    dto.setClub_url("clubs/" + dto.getClubId());

    return dto;
  }
  public List<TrainingDto> mapToTrainingDto(Iterable<Training> trainings) {
    List<TrainingDto> dtos = new ArrayList<>();
    for(Training t: trainings)
      dtos.add(mapToTrainingDto(t));
    return dtos;
  }
  public List<List<TrainingDto>> mapToTrainingDto(List<List<Training>> trainingsByDay) {
    List<List<TrainingDto>> dtoList = new ArrayList<>();
    for(List<Training> dayList: trainingsByDay)
      dtoList.add(mapToTrainingDto(dayList));
    return dtoList;
  }

  public Training mapFromTrainingDto(TrainingDto dto) {
    Training tr = new Training();
    tr.setDateTime(dto.getDate().atTime(dto.getStartTime()));
    tr.setLastDate(dto.getLastDate());
    tr.setWeekNum(dto.getWeekNum());
    tr.setDurationMinutes(dto.getDurationMinutes());
    tr.setCourt(dto.getCourt());
    tr.setBulletPoints(dto.getBulletPoints());
    tr.setComment(dto.getComment());
    tr.setIsFree(dto.isFree());
    if(dto.getAttendeeIds() != null)
      tr.setAttendees(dto.getAttendeeIds().stream().map(id -> userService.loadUser(id)).collect(Collectors.toSet()));
    tr.setTrainingGroup(groupService.loadTrainingGroupById(dto.getGroupId()));
    tr.setTrainer(userService.loadUser(dto.getTrainerId()));
    tr.setPrevTrainer(userService.loadUser(dto.getPrevTrainerId()));
    tr.setClub(clubService.loadClub(dto.getClubId()));
    return tr;
  
  
  /*
    tr.setTrainingGroup(trainingGroupService.loadTrainingGroupById(dto.getGroup().getId()));
    tr.setTrainer(userService.loadUser(dto.getTrainer().getId()));
    tr.setClub(clubService.loadClub(dto.getClubId()));
    tr.setCourt(dto.getCourt());
    tr.setDateTime(dto.getDate().atTime(dto.getStartTime()));
    tr.setLastDate(dto.getLastDate());
    tr.setDurationMinutes(dto.getDurationMinutes());
    if(dto.getAttendees() != null)
      tr.setAttendees(dto.getAttendees().stream().map(id -> userService.loadUser(id))
                                                 .collect(Collectors.toSet()));
    tr.setBulletPoints(dto.getBulletPoints());
    tr.setComment(dto.getComments());
    tr.setIsFree(dto.isFree());
    */
  }

  public TimetableDto mapToTimetableDto(List<String> dates, List<List<TrainingDto>> trsByDay) {
    TimetableDto dto = new TimetableDto();
    dto.setDatesInWeek(dates);
    dto.setTrainings(trsByDay);
    return dto;
  }

}
