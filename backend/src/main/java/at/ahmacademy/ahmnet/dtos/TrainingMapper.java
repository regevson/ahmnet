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
  TrainingGroupService trainingGroupService;
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

  public List<List<TrainingSnippetDto>> mapToTrainingSnippetDto(List<List<Training>> trainingsByDay) {
    List<List<TrainingSnippetDto>> dtoList = new ArrayList<>();
    for(List<Training> dayList: trainingsByDay)
      dtoList.add(mapToTrainingSnippetDto(dayList));
    return dtoList;
  }

  public TrainingDto mapToTrainingDto(Training tr) {
    TrainingDto dto = new TrainingDto();
    dto.setId(tr.getId());
    dto.setGroup(groupMapper.mapToTrainingGroupSnippetDto(tr.getTrainingGroup()));
    dto.setClubId(tr.getClub().getId());
    dto.setCourt(tr.getCourt());
    dto.setDate(tr.getDateTime().toLocalDate());
    dto.setLastDate(tr.getLastDate());
    LocalTime startTime = tr.getDateTime().toLocalTime();
    dto.setStartTime(startTime);
    dto.setTimeslot(startTime.toString() + " - " + startTime.plusMinutes(tr.getDurationMinutes()).toString());
    dto.setDurationMinutes(tr.getDurationMinutes());
    dto.setTrainer(userMapper.mapToUserDto(tr.getTrainer()));
    dto.setPlayers(userMapper.mapToUserDto(tr.getTrainingGroup().getPlayers()));
    dto.setAttendees(tr.getAttendees().stream().map(u -> u.getId()).collect(Collectors.toList()));
    dto.setBulletPoints(tr.getBulletPoints());
    dto.setComments(tr.getComment());
    dto.setFree(tr.getIsFree());
    return dto;
  }

  public void mapFromTrainingDto(TrainingDto dto, Training tr) {
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
  }

  public TimetableDto mapToTimetableDto(List<String> dates, List<List<TrainingSnippetDto>> trsByDay) {
    TimetableDto dto = new TimetableDto();
    dto.setDatesInWeek(dates);
    dto.setTrainings(trsByDay);
    return dto;
  }

}
