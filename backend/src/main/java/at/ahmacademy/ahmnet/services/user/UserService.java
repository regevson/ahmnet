package at.ahmacademy.ahmnet.services.user;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.model.UserRole;
import at.ahmacademy.ahmnet.repositories.UserRepository;

@Service
@Scope("application")
public class UserService {

  @Autowired
  private UserRepository userRepository;

  /**
   * Returns a collection of all users.
   *
   * @return
   */
  @PreAuthorize("hasAuthority('ADMIN')")
  public Collection<User> getAllUsers() {
    return userRepository.findAll();
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
  public Collection<User> getUsersByRole(Optional<UserRole> role) {
    if(role.isPresent())
      return userRepository.findByRole(role.get());
    else
      return userRepository.findAll();
  }

  /**
   * Loads a single user identified by its username.
   *
   * @param username the username to search for
   * @return the user with the given username
   */
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('TRAINER') or authentication.getName() eq #username")
  public User loadUser(String username) {
    return userRepository.findFirstByUsername(username);
  }

  /**
   * Some user (admin) saves a specific user. This method will also set {@link User#createDate} for new
   * entities or {@link User#updateDate} for updated entities. The user
   * requesting this operation will also be stored as {@link User#createDate}
   * or {@link User#updateUser} respectively.
   *
   * @param user the user to save
   * @return the updated user
   */
  @PreAuthorize("hasAuthority('ADMIN') or authentication.getName() eq #user.getUsername()")
  public User saveUser(User user) {
    if(user.isNew()) {
      user.setCreateDate(new Date());
      user.setCreateUser(getAuthUser());
      user.setId(user.getFirstName() + user.getLastName());
    } 
    else {
      user.setUpdateDate(new Date());
      user.setUpdateUser(getAuthUser());
    }
    return userRepository.save(user);
  }

  /**
   * Deletes the user.
   *
   * @param user the user to delete
   */
  @PreAuthorize("hasAuthority('ADMIN')")
  public void deleteUser(User user) {
    userRepository.delete(user);
  }

  public User getAuthUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User user = userRepository.findFirstByUsername(auth.getName());
    return user;
  }

  @PreAuthorize("hasAuthority('ADMIN') or authentication.getName() eq #userId")
  public void changePassword(String userId, String password) {
    User user = loadUser(userId);
    user.setPassword(password);
    this.saveUser(user);
  }

  public boolean isAdmin() {
    return getAuthUser().getRoles().contains(UserRole.ADMIN);
  }

  public boolean isAdmin(User user) {
    return user.getRoles().contains(UserRole.ADMIN);
  }

  public boolean isTrainer() {
    return getAuthUser().getRoles().contains(UserRole.TRAINER);
  }

  public boolean hasTrainerRights() {
    return isAdmin() || isTrainer();
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
  public Set<User> loadUsersByClubAndRole(String clubName, Optional<UserRole> role) {
    Set<User> members = this.userRepository.findByClub_IdContaining(clubName);
    if(role.isPresent())
      members = members.stream().filter(m -> m.getRoles().contains(role.get())).collect(Collectors.toSet());
    return members;
  }

}
