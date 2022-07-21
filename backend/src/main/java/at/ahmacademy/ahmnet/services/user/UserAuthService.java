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


}
