package at.ahmacademy.ahmnet.services.training;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.repositories.TrainingRepository;
import at.ahmacademy.ahmnet.services.user.UserPathValidatorService;

@Service
@Scope("application")
public class TrainingPathValidationService {

  @Autowired
  private UserPathValidatorService userValidator;
  @Autowired
  private TrainingRepository trainingRepo;

  public List<Training> trainingBelongsToTrainer(String trainerId, Long[] trainingIds) {
    return Stream.of(trainingIds).map(id -> trainingBelongsToTrainer(trainerId, id)).collect(Collectors.toList());
  }

  public Training trainingBelongsToTrainer(String trainerId, Long trainingId) {
    userValidator.validateTrainer(trainerId);
    Training training = trainingExists(trainingId);
    if(!training.getTrainer().getId().equals(trainerId))
      throw new IllegalArgumentException("Specified training doesn't belong to specified trainer");
    return training;
  }

  public Training trainingExists(Long trainingId) {
    return trainingRepo.findById(trainingId).orElseThrow(() 
          -> new IllegalArgumentException("Specified training doesn't exist!"));
  }

  public List<Training> trainingExists(Long[] trainingIds) {
    return Stream.of(trainingIds).map(id -> trainingExists(id)).collect(Collectors.toList());
  }

  public List<Training> trainingBelongsToTrainer(String[] trainerIds, Long[] trainingIds) {
    if(trainerIds.length != trainingIds.length)
      throw new IllegalArgumentException("Specified TrainerIds and TrainingIds of different size!");
      
    List<Training> trainings = new ArrayList<>();
    for(int i = 0; i < trainerIds.length; i++)
      trainings.add(trainingBelongsToTrainer(trainerIds[i], trainingIds[i]));
    return trainings;
  }

}
