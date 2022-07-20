package at.ahmacademy.ahmnet.services.user;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.services.AuthService;

@Service
@Scope("application")
public class UserAuthService extends AuthService<User> {

  @Autowired
  private UserService userService;

  public boolean authUsrIsAdmin() {
    return userService.isAdmin();
  }

  public boolean authUsrIsTrainer() {
    return userService.isTrainer();
  }

  public boolean isAuthUsr(String userId) {
    return userService.getAuthUser().getId().equals(userId);
  }

  public boolean isUnderWing(String playerId) {
    if(playerId == null) return true;
    User me = userService.getAuthUser();
    User player = userService.loadUser(playerId);
    Set<User> trainer = player.getTrainingGroups().stream().map(g -> g.getTrainer()).collect(Collectors.toSet());
    if(trainer.isEmpty()) return true;
    return trainer.stream().anyMatch(t -> t.getId() == me.getId());
  }

}
