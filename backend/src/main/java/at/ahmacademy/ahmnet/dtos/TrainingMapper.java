package at.ahmacademy.ahmnet.dtos;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.services.club.ClubService;
import at.ahmacademy.ahmnet.services.training.TrainingService;
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

  public TrainingResponse mapToDto(Training tr) {
    TrainingResponse dto = new TrainingResponse();
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
    dto.setComments(tr.getComments());
    dto.setFree(tr.getIsFree());
    dto.setPlayerIds(tr.getTrainingGroup().getPlayers().stream().map(User::getId).collect(Collectors.toSet()));
    dto.setAttendeeIds(tr.getAttendees().stream().map(User::getId).collect(Collectors.toList()));
    dto.setGroupId(tr.getTrainingGroup().getId());
    dto.setTrainerId(tr.getTrainer().getId());
    dto.setPrevTrainerId(tr.getPrevTrainer().getId());
    dto.setClubId(tr.getClub().getId());

    dto.setPlayers_url("users/" + dto.getPlayerIds().stream().collect(Collectors.joining(",")));
    dto.setAttendees_url("users/" + dto.getAttendeeIds());
    dto.setGroup_url("groups/" + dto.getGroupId());
    dto.setTrainer_url("users/" + dto.getTrainerId());
    dto.setPrevTrainer_url("users/" + dto.getPrevTrainerId());
    dto.setClub_url("clubs/" + dto.getClubId());

    return dto;
  }
  public List<TrainingResponse> mapToDto(Iterable<Training> trainings) {
    List<TrainingResponse> dtos = new ArrayList<>();
    for(Training t: trainings)
      dtos.add(mapToDto(t));
    return dtos;
  }
  public List<List<TrainingResponse>> mapToDto(List<List<Training>> trainingsByDay) {
    List<List<TrainingResponse>> dtoList = new ArrayList<>();
    for(List<Training> dayList: trainingsByDay)
      dtoList.add(mapToDto(dayList));
    return dtoList;
  }

  public Training mapToEntity(Long id, TrainingRequest dto) {
    Training tr = new Training();
    if(id != null)
      tr = trainingService.loadTrainingById(id);
    tr.setDateTime(dto.getDate().atTime(dto.getStartTime()));
    tr.setLastDate(dto.getLastDate());
    tr.setDurationMinutes(dto.getDurationMinutes());
    tr.setCourt(dto.getCourt());
    tr.setBulletPoints(dto.getBulletPoints());
    tr.setComments(dto.getComments());
    tr.setIsFree(dto.isFree());
    if(dto.getAttendeeIds() != null)
      tr.setAttendees(dto.getAttendeeIds().stream().map(i -> userService.loadUser(i)).collect(Collectors.toSet()));
    tr.setTrainingGroup(groupService.loadGroupById(dto.getGroupId()));
    tr.setTrainer(userService.loadUser(dto.getTrainerId()));
    tr.setClub(clubService.loadClub(dto.getClubId()));

    return tr;
  }

  public TimetableDto mapToTimetableDto(List<String> dates, List<List<TrainingResponse>> trsByDay) {
    TimetableDto dto = new TimetableDto();
    dto.setDatesInWeek(dates);
    dto.setTrainings(trsByDay);
    return dto;
  }

}
