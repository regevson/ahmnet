package at.ahmacademy.ahmnet.controllers;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.ahmacademy.ahmnet.dtos.TrainingGroupDto;
import at.ahmacademy.ahmnet.dtos.TrainingGroupMapper;
import at.ahmacademy.ahmnet.dtos.TrainingGroupSnippetDto;
import at.ahmacademy.ahmnet.model.Club;
import at.ahmacademy.ahmnet.model.TrainingGroup;
import at.ahmacademy.ahmnet.services.trainingGroup.TrainingGroupPathValidationService;
import at.ahmacademy.ahmnet.services.trainingGroup.TrainingGroupService;

@RequestMapping("/api")
@RestController
@Scope("application")
public class TrainingGroupController {

  @Autowired
  TrainingGroupService groupService;
  @Autowired
  TrainingGroupMapper mapper;
  @Autowired
  TrainingGroupPathValidationService pathValidator;

  @GetMapping("/clubs")
  public ResponseEntity<?> getAllClubs() {
    Collection<Club> clubs = groupService.loadAllClubs();
    return ResponseEntity.status(HttpStatus.OK).body(clubs);
  }

  @GetMapping("/clubs/actions/count-groups")
  public ResponseEntity<?> getAllClubsWithGroupCount() {
    Collection<Club> clubs = groupService.loadAllClubs();
    Map<String, Integer> club_groupNum = groupService.getNumOfGroups(clubs);
    return ResponseEntity.status(HttpStatus.OK).body(club_groupNum);
  }

  @GetMapping("/batch/groups")
  public ResponseEntity<?> getAllGroups() {
    Collection<TrainingGroupSnippetDto> dtos =
      mapper.mapToTrainingGroupSnippetDto(groupService.loadAllGroups());
    return ResponseEntity.status(HttpStatus.OK).body(dtos);
  }

  @DeleteMapping("/clubs/{clubId}/groups/{groupId}")
  public ResponseEntity<?> deleteGroup(@PathVariable String clubId, @PathVariable Long groupId) {
    TrainingGroup group = pathValidator.validatePath(clubId, groupId);
    groupService.deleteGroup(group);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/clubs/{clubId}/groups")
  public ResponseEntity<?> getGroupsByClub(@PathVariable String clubId) {
    pathValidator.validatePath(clubId);
    Collection<TrainingGroupSnippetDto> dtos =
      mapper.mapToTrainingGroupSnippetDto(groupService.loadTrainingGroupsByClub(clubId));
    return ResponseEntity.status(HttpStatus.OK).body(dtos);
  }

  @GetMapping("/clubs/{clubId}/groups/{groupId}")
  public ResponseEntity<?> getGroupById(@PathVariable String clubId, @PathVariable Long groupId) {
    TrainingGroup group = pathValidator.validatePath(clubId, groupId);
    int numPlayedTr = groupService.calcNumPlayedSessions(group);
    Map<String, Integer> attendance = groupService.calcAttendance(group);
    TrainingGroupDto dto = mapper.mapToTrainingGroupDto(group, numPlayedTr, attendance);
    return ResponseEntity.status(HttpStatus.OK).body(dto);
  }

  @PostMapping("/clubs/{id}/groups")
  public ResponseEntity<?> createNewGroup(@PathVariable String id, @RequestBody TrainingGroupDto groupDto) {
    pathValidator.validatePath(id);
    TrainingGroup group = new TrainingGroup();
    mapper.mapFromTrainingGroupDto(groupDto, group);
    groupService.saveGroup(group);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PutMapping("/clubs/{clubId}/groups/{groupId}")
  public ResponseEntity<?> updateTrainingGroupDetails(@PathVariable String clubId,
                                                      @PathVariable Long groupId,
                                                      @RequestBody TrainingGroupDto groupDto) {
    TrainingGroup group = pathValidator.validatePath(clubId, groupDto.getId());
    mapper.mapFromTrainingGroupDto(groupDto, group);
    groupService.saveGroup(group);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

}
