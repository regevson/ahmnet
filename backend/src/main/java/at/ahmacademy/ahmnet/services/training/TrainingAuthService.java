package at.ahmacademy.ahmnet.services.training;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.services.user.UserService;

@Service
@Scope("application")
public class TrainingAuthService {

  @Autowired
  UserService userService;


  public void whenUserIsTheTrainer(User user, Training training) {
    String userId = user.getId();
    String trainerId = training.getTrainer().getId();
    if(!userService.isAdmin(user) && !userId.equals(trainerId))
      throw new IllegalArgumentException("Trainer in path is not trainer of this training!");
  }

  public void whenUserIsTheTrainer(User user, List<Training> trainings) {
    for(Training training: trainings)
      whenUserIsTheTrainer(user, training);
  }

  public void whenTrainingIsFree(Training training) {
    User authUser = userService.getAuthenticatedUser();
    boolean isFree = training.getIsFree();
    if(!userService.isAdmin(authUser) && !isFree)
      throw new IllegalArgumentException("Training is not free!");
  }

  public void whenTrainingIsFree(List<Training> trainings) {
    for(Training training: trainings)
      whenTrainingIsFree(training);
  }

}
