package at.ahmacademy.ahmnet.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.services.user.UserService;

@Component
@Scope("view")
public class UserDetailController {

  @Autowired
  private UserService userService;

  /**
   * Attribute to cache the currently displayed user
   */
  private User user;

  /**
   * Sets the currently displayed user and reloads it form db. This user is
   * targeted by any further calls of
   * {@link #doReloadUser()}, {@link #doSaveUser()} and
   * {@link #doDeleteUser()}.
   *
   * @param user
   */
  public void setUser(User user) {
    this.user = user;
    doReloadUser();
  }

  /**
   * Returns the currently displayed user.
   *
   * @return
   */
  public User getUser() {
    return user;
  }

  /**
   * Action to force a reload of the currently displayed user.
   */
  public void doReloadUser() {
    user = userService.loadUser(user.getUsername());
  }

  /**
   * Action to save the currently displayed user.
   */
  public void doSaveUser() {
    user = this.userService.saveUser(user);
    this.user = null;
  }

  /**
   * Action to delete the currently displayed user.
   */
  public void doDeleteUser() {
    this.userService.deleteUser(user);
    user = null;
  }

}
