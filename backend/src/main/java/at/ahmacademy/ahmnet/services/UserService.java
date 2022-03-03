package at.ahmacademy.ahmnet.services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.AccessDeniedException;
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
    public Collection<User> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
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
        if (user.isNew()) {
            user.setCreateDate(new Date());
            user.setCreateUser(getAuthenticatedUser());
        } else {
            user.setUpdateDate(new Date());
            user.setUpdateUser(getAuthenticatedUser());
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
        // :TODO: write some audit log stating who and when this user was permanently deleted.
    }

    // get user that is currently logged in
    public User getAuthenticatedUser() {
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

    public boolean isAuthUser(String someUserId) {
	return getAuthenticatedUser().getId().equals(someUserId);
    }

    public boolean isAdmin() {
	return getAuthenticatedUser().getRoles().contains(UserRole.ADMIN);
    }

    public boolean isTrainer() {
	return getAuthenticatedUser().getRoles().contains(UserRole.TRAINER);
    }

    public boolean hasTrainerRights() {
	return isAdmin() || isTrainer();
    }
    
}
