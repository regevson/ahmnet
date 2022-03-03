package at.ahmacademy.ahmnet.services;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import at.ahmacademy.ahmnet.model.Club;
import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.model.TrainingGroup;
import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.repositories.ClubRepository;
import at.ahmacademy.ahmnet.repositories.TrainingGroupRepository;

@Service
@Scope("application")
public class TrainingGroupService {

    @Autowired
    TrainingGroupRepository trainingGroupRepository;
    @Autowired
    ClubRepository clubRepository;
    @Autowired
    UserService userService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public List<Club> loadAllClubs() {
        return clubRepository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public List<TrainingGroup> loadAllGroups() {
        return trainingGroupRepository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public Set<TrainingGroup> loadTrainingGroupsByClub(String clubName) {
	return this.trainingGroupRepository.findByClub_NameContaining(clubName);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public TrainingGroup loadTrainingGroupById(String clubId, long groupId) {
	TrainingGroup group = this.trainingGroupRepository.findById(groupId).orElse(null);
	authorizeGroup(clubId, group);
	return group;
    }

    @PreAuthorize("hasAuthority('ADMIN') or authentication.getName() eq #group.trainer.getId")
    public TrainingGroup saveGroup(TrainingGroup group) {
        return trainingGroupRepository.save(group);
    }

    @PreAuthorize("hasAuthority('ADMIN') or authentication.getName() eq #group.trainer.getId")
    public void deleteGroup(TrainingGroup group) {
	group.getPlayers().removeAll(group.getPlayers());
        trainingGroupRepository.delete(group);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public Map<String, Integer> calcAttendance(TrainingGroup group) {
	Map<String, Integer> attendance = new HashMap<>();
	Set<Training> trainings = group.getTrainings();
	List<User> attendees = new LinkedList<>();
	for(Training t : trainings)
	    attendees.addAll(t.getAttendees());
	for(User p : group.getPlayers())
	   attendance.put(p.getId(), Collections.frequency(attendees, p)); 
	return attendance;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public int calcNumPlayedSessions(TrainingGroup trg) {
	return this.trainingGroupRepository.countPlayedTrainingsByGroupId(trg.getId(), LocalDateTime.now());
    }

    public Map<String, Integer> getNumOfGroups(Collection<Club> clubs) {
	Map<String, Integer> map = new HashMap<>();
	for(Club club : clubs)
	    map.put(club.getName(), this.loadTrainingGroupsByClub(club.getName()).size());
	return map;
    }
    
    private void authorizeGroup(String pathClubId, TrainingGroup group) {
	String clubId = group.getClub().getName();
	if(!pathClubId.equals(clubId))
	    throw new IllegalArgumentException("Club in path is not club of this group!");
	if(!userService.hasTrainerRights())
	    throw new AccessDeniedException("No rights to view group!");
    }

}
