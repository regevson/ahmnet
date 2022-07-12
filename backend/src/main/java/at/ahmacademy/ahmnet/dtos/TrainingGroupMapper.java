package at.ahmacademy.ahmnet.dtos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.model.TrainingGroup;
import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.services.trainingGroup.ClubService;
import at.ahmacademy.ahmnet.services.trainingGroup.TrainingGroupService;
import at.ahmacademy.ahmnet.services.user.UserService;

@Component
public class TrainingGroupMapper {

  @Autowired
  UserService userService;
  @Autowired
  TrainingGroupService groupService;
  @Autowired
  ClubService clubService;
  @Autowired
  ClubMapper clubMapper;
  @Autowired
  UserMapper userMapper;

  public GroupResponse mapToDto(TrainingGroup group) {
    GroupResponse dto = new GroupResponse();
    dto.setId(group.getId());
    dto.setTrainerId(group.getTrainer().getId());
    dto.setClubId(group.getClub().getId());
    dto.setPlayerIds(group.getPlayers().stream().map(User::getId).collect(Collectors.toSet()));
    dto.setAttendance(group.getAttendance());
    dto.setNumPlayedSessions(group.getNumPlayedSessions());

    dto.setTrainer_url("trainer/" + dto.getTrainerId());
    String playerClubIds = group.getPlayers().stream().map(a -> a.getClub().getId()).collect(Collectors.joining(","));
    dto.setPlayers_url("clubs/" + playerClubIds + "/players/" + dto.getPlayerIds().stream().collect(Collectors.joining(",")));
    dto.setClub_url("clubs/" + dto.getClubId());
    return dto;
  }
  public Collection<GroupResponse> mapToDto(Collection<TrainingGroup> groups) {
    Collection<GroupResponse> dtos = new ArrayList<>();
    for(TrainingGroup tg: groups)
      dtos.add(mapToDto(tg));
    return dtos;
  }

  public TrainingGroup mapToEntity(Long id, GroupRequest dto) {
    TrainingGroup group = new TrainingGroup();
    if(id != null)
      group = groupService.loadTrainingGroupById(id);
    group.setTrainer(userService.loadUser(dto.getTrainerId()));
    group.setClub(clubService.loadClub(dto.getClubId()));
    Set<User> players = dto.getPlayerIds().stream().map(p -> userService.loadUser(p)).collect(Collectors.toSet());
    group.setPlayers(players);
    return group;
  }

  public TrainingGroupSnippetDto mapToTrainingGroupSnippetDto(TrainingGroup trg) {
    TrainingGroupSnippetDto dto = new TrainingGroupSnippetDto();
    dto.setId(trg.getId());
    dto.setTrainer(userMapper.mapToUserDto(trg.getTrainer()));
    dto.setClub(clubMapper.mapToClubDto(trg.getClub()));
    dto.setPlayers(userMapper.mapToUserDto(trg.getPlayers()));
    return dto;
  }

  public Collection<TrainingGroupSnippetDto> mapToTrainingGroupSnippetDto
                                                       (Collection<TrainingGroup> groups) {
    Collection<TrainingGroupSnippetDto> dtos = new ArrayList<>();
    for(TrainingGroup tg: groups)
      dtos.add(mapToTrainingGroupSnippetDto(tg));
    return dtos;
  }

}
