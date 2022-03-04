package at.ahmacademy.ahmnet.services.user;

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

}
