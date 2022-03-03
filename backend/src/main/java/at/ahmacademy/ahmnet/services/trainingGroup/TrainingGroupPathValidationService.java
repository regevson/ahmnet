package at.ahmacademy.ahmnet.services.trainingGroup;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import at.ahmacademy.ahmnet.model.Club;
import at.ahmacademy.ahmnet.model.TrainingGroup;
import at.ahmacademy.ahmnet.repositories.ClubRepository;
import at.ahmacademy.ahmnet.repositories.TrainingGroupRepository;

@Service
@Scope("application")
public class TrainingGroupPathValidationService {

  @Autowired
  private TrainingGroupRepository groupRepo;
  @Autowired
  private ClubRepository clubRepo;


  public Club validatePath(String clubId) {
    return clubRepo.findById(clubId).orElseThrow(() 
        -> new IllegalArgumentException("Group specified in request doesn't belong "
            + "to club specified in request!"));
  }

  public TrainingGroup validatePath(String clubId, Long groupId) {
    TrainingGroup group = groupRepo.findById(groupId).orElseThrow();
    if(!group.getClub().getName().equals(clubId))
      throw new IllegalArgumentException("Group specified in request doesn't belong "
          + "to club specified in request!");
    return group;
  }

  public List<TrainingGroup> validatePath(String clubId, Long[] groupIds) {
    List<TrainingGroup> groups = new ArrayList<>();
    for(Long id: groupIds)
      groups.add(validatePath(clubId, id));
    return groups;
  }

}
