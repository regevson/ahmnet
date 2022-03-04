package at.ahmacademy.ahmnet.services.training;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.repositories.TrainingRepository;

@Service
@Scope("application")
public class TrainingPathValidationService {

  @Autowired
  private TrainingRepository trainingRepo;


  public Training validatePath(String trainerId, Long trainingId) {
    Training training = validatePath(trainingId);
    if(!training.getTrainer().getId().equals(trainerId))
        throw new IllegalArgumentException("Training specified in request doesn't belong "
            + "to trainer specified in request!");
    return training;
  }

  public List<Training> validatePath(String trainerId, Long[] trainingIds) {
    List<Training> trainings = new ArrayList<>();
    for(Long id : trainingIds)
      trainings.add(validatePath(trainerId, id));
    return trainings;
  }

  public Training validatePath(Long trainingId) {
    return trainingRepo.findById(trainingId).orElseThrow(() 
          -> new IllegalArgumentException("Training specified in request doesn't exist!"));
  }

  public List<Training> validatePath(Long[] trainingIds) {
    List<Training> trainings = new ArrayList<>();
    for(Long id : trainingIds)
      trainings.add(validatePath(id));
    return trainings;
  }

}
