package at.ahmacademy.ahmnet.services.training;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.services.AuthService;

@Service
@Scope("application")
public class TrainingAuthService extends AuthService<Training> {

  public boolean hasTrainer(Training t, String userId) {
    return userId.equals(t.getTrainer().getId());
  }

  public boolean isFree(Training training) {
    return training.getIsFree();
  }

}
