package at.ahmacademy.ahmnet.dtos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.ahmacademy.ahmnet.model.TrainingGroup;
import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.services.trainingGroup.TrainingGroupService;
import at.ahmacademy.ahmnet.services.user.UserService;

@Component
public class TrainingGroupMapper {

  @Autowired
  UserService userService;
  @Autowired
  TrainingGroupService trainingGroupService;
  @Autowired
  ClubMapper clubMapper;
  @Autowired
  UserMapper userMapper;

  public TrainingGroupDto mapToTrainingGroupDto(TrainingGroup trg, 
                                                int numPlayedSessions, 
                                                Map<String, Integer> attendance) {
    TrainingGroupDto dto = new TrainingGroupDto();
    dto.setId(trg.getId());
    dto.setTrainer(userMapper.mapToUserDto(trg.getTrainer()));
    dto.setClub(clubMapper.mapToClubDto(trg.getClub()));
    dto.setPlayers(userMapper.mapToUserDto(trg.getPlayers()));
    dto.setNumPlayedSessions(numPlayedSessions);
    dto.setAttendance(attendance);
    return dto;
  }

  public void mapFromTrainingGroupDto(TrainingGroupDto dto, TrainingGroup trg) {
    trg.setTrainer(this.userService.loadUser(dto.getTrainer().getId()));
    trg.setClub(clubMapper.mapFromClubDto(dto.getClub()));
    Set<User> players = dto.getPlayers().stream().map(p -> userService.loadUser(p.getId()))
                                                 .collect(Collectors.toSet());
    trg.setPlayers(players);
    trg.setNumPlayedSessions(dto.getNumPlayedSessions());
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
