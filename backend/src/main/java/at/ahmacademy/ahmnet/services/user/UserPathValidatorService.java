package at.ahmacademy.ahmnet.services.user;

import static at.ahmacademy.ahmnet.model.UserRole.TRAINER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.repositories.UserRepository;

@Service
@Scope("application")
public class UserPathValidatorService {

  @Autowired
  private UserRepository userRepo;

  public User validateTrainer(String trainerId) {
    User trainer = userRepo.findById(trainerId).orElseThrow(() 
          -> new IllegalArgumentException("Specified trainer doesn't exist!"));
    if(!trainer.getRoles().contains(TRAINER))
      throw new IllegalArgumentException("Specified user is not a trainer!");
    return trainer;
  }

}
