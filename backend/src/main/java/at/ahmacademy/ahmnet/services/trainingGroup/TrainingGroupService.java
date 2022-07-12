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
import org.springframework.transaction.annotation.Transactional;

import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.model.TrainingGroup;
import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.repositories.TrainingGroupRepository;
import at.ahmacademy.ahmnet.services.training.TrainingService;
import at.ahmacademy.ahmnet.services.user.UserService;

@Service
@Scope("application")
public class TrainingGroupService {

  @Autowired
  private TrainingGroupRepository trainingGroupRepository;
  @Autowired
  private TrainingService trainingService;
  @Autowired
  private UserService userService;
  /*
  @Autowired
  private TrainingGroupAuthService auth;
  */

  @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
  public List<TrainingGroup> loadAllGroups() {
    return trainingGroupRepository.findAll();
  }

  @PreAuthorize("hasAuthority('ADMIN') || @userAuthService.isAuthUsr(#group.trainer.id)")
  public TrainingGroup saveGroup(TrainingGroup group) {
    return trainingGroupRepository.save(group);
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
  public TrainingGroup loadTrainingGroupById(long groupId) {
    TrainingGroup group = this.trainingGroupRepository.findById(groupId).orElse(null);
    return group;
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
  public Set<TrainingGroup> loadTrainingGroupsByClub(String clubName) {
    return this.trainingGroupRepository.findByClub_IdContaining(clubName);
  }

  @PreAuthorize("hasAuthority('ADMIN') || @userAuthService.isAuthUsr(#group.trainer.id)")
  public void deleteGroup(TrainingGroup group) {
    group.getPlayers().stream().forEach(p -> p.getTrainingGroups().remove(group)); 
    group.getPlayers().stream().forEach(p -> userService.saveUser(p));
    group.setPlayers(Set.of());
    group.getTrainings().stream().forEach(t -> t.setTrainingGroup(null));
    group.getTrainings().stream().forEach(t -> t.setAttendees(Set.of()));
    group.getTrainings().stream().forEach(t -> trainingService.updateTraining(t));
    group.setTrainings(null);
    this.saveGroup(group);
    try {
      trainingGroupRepository.delete(group);
      System.out.println("after deleted");
    }catch(Exception ex) {ex.printStackTrace();}
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
    int num = this.trainingGroupRepository.countPlayedTrainingsByGroupId(group.getId(), LocalDateTime.now());
    group.setNumPlayedSessions(num);
  }


}
