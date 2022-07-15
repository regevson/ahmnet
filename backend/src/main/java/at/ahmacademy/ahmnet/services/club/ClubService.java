package at.ahmacademy.ahmnet.services.club;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import at.ahmacademy.ahmnet.model.Club;
import at.ahmacademy.ahmnet.repositories.ClubRepository;
import at.ahmacademy.ahmnet.services.trainingGroup.TrainingGroupService;
import at.ahmacademy.ahmnet.services.user.UserService;

@Service
@Scope("application")
public class ClubService {

  @Autowired
  private ClubRepository clubRepository;
  @Autowired
  private TrainingGroupService groupService;
  @Autowired
  private UserService userService;

  public Club loadClub(String name) {
    return clubRepository.findFirstById(name);
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
  public List<Club> loadAllClubs() {
    return clubRepository.findAll();
  }


  @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
  public Map<String, Integer> getNumOfPlayers(Collection<Club> clubs) {
    Map<String, Integer> map = new HashMap<>();
    for(Club club: clubs)
      map.put(club.getId(), userService.loadPlayersByClub(club.getId()).size());
    return map;
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
  public Map<String, Integer> getNumOfGroups(Collection<Club> clubs) {
    Map<String, Integer> map = new HashMap<>();
    for(Club club: clubs)
      map.put(club.getId(), groupService.loadGroupsByClub(club.getId()).size());
    return map;
  }

}
