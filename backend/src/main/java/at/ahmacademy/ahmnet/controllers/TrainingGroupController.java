package at.ahmacademy.ahmnet.controllers;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

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

import at.ahmacademy.ahmnet.dtos.GroupRequest;
import at.ahmacademy.ahmnet.dtos.GroupResponse;
import at.ahmacademy.ahmnet.dtos.TrainingGroupMapper;
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

  @GetMapping("/groups")
  public ResponseEntity<?> getAllGroups() {
    Collection<GroupResponse> dtos = mapper.mapToDto(groupService.loadAllGroups());
    return ResponseEntity.status(HttpStatus.OK).body(dtos);
  }

  @DeleteMapping("/groups/{groupId}")
  public ResponseEntity<?> deleteGroup(@PathVariable Long groupId) {
    TrainingGroup group = groupService.loadTrainingGroupById(groupId);
    groupService.deleteGroup(group);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/clubs/{clubId}/groups")
  public ResponseEntity<?> getGroupsByClub(@PathVariable String clubId) {
    Collection<GroupResponse> dtos = mapper.mapToDto(groupService.loadTrainingGroupsByClub(clubId));
    return ResponseEntity.status(HttpStatus.OK).body(dtos);
  }

  @GetMapping("/groups/{groupId}")
  public ResponseEntity<?> getGroupById(@PathVariable Long[] groupId) {
    Collection<TrainingGroup> groups = Arrays.stream(groupId).map(id -> groupService.loadTrainingGroupById(id))
                                                             .collect(Collectors.toSet());
    groups.stream().forEach(g -> groupService.calcAttendance(g));
    groups.stream().forEach(g -> groupService.calcNumPlayedSessions(g));
    Collection<GroupResponse> dtos = mapper.mapToDto(groups);
    if(dtos.size() == 1) return ResponseEntity.status(HttpStatus.OK).body(dtos.iterator().next());
    return ResponseEntity.status(HttpStatus.OK).body(dtos);
  }

  @PostMapping("/groups")
  public ResponseEntity<?> createNewGroup(@RequestBody GroupRequest groupDto) {
    TrainingGroup group = mapper.mapToEntity(null, groupDto);
    groupService.saveGroup(group);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PutMapping("/groups/{groupId}")
  public ResponseEntity<?> updateTrainingGroupDetails(@PathVariable Long groupId,
                                                      @RequestBody GroupRequest groupDto) {
    TrainingGroup group = mapper.mapToEntity(groupId, groupDto);
    groupService.saveGroup(group);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

}
