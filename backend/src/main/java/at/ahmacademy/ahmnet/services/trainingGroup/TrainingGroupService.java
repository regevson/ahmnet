package at.ahmacademy.ahmnet.services.trainingGroup;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.model.TrainingGroup;
import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.repositories.TrainingGroupRepository;
import at.ahmacademy.ahmnet.services.user.UserService;

@Service
@Scope("application")
public class TrainingGroupService {

  private TrainingGroupRepository groupRepository;


  @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
  public List<TrainingGroup> loadAllGroups() {
    return groupRepository.findAll();
  }

  @PreAuthorize("hasAuthority('ADMIN') || @userService.isAuthUsr(#group.trainer.id)")
  public TrainingGroup saveGroup(TrainingGroup group) {
    return groupRepository.save(group);
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
  public TrainingGroup loadGroupById(long groupId) {
    TrainingGroup group = this.groupRepository.findById(groupId).orElse(null);
    return group;
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
  public Set<TrainingGroup> loadGroupsByClub(String clubName) {
    return this.groupRepository.findByClub_IdContaining(clubName);
  }

  @PreAuthorize("hasAuthority('ADMIN') || @userService.isAuthUsr(#group.trainer.id)")
  public void deleteGroup(TrainingGroup group) {
    groupRepository.delete(group);
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
  public void calcAttendance(TrainingGroup group) {
    Map<String, Integer> attendance = new HashMap<>();
    Set<Training> trainings = group.getTrainings();
    List<User> attendees = new LinkedList<>();
    for(Training t: trainings)
      attendees.addAll(t.getAttendees());
    for(User p: group.getPlayers())
      attendance.put(p.getId(), Collections.frequency(attendees, p));
    group.setAttendance(attendance);
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
  public void calcNumPlayedSessions(TrainingGroup group) {
    int num = this.groupRepository.countPlayedTrainingsByGroupId(group.getId(), LocalDateTime.now());
    group.setNumPlayedSessions(num);
  }


  @Autowired
  public void setGroupRepository(TrainingGroupRepository groupRepository) {
    this.groupRepository = groupRepository;
  }

}
