package at.qe.skeleton.services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import at.qe.skeleton.model.AbstractUser;
import at.qe.skeleton.model.User;
import at.qe.skeleton.repositories.AbstractUserRepository;

@Service
@Scope("application")
public abstract class AbstractUserService<U extends AbstractUser, R extends AbstractUserRepository<U>> {

    @Autowired
    protected R userRepository;

    /**
     * Returns a collection of all users.
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<U> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #username")
    public U loadUser(String username) {
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public U saveUser(U user) {
        if (user.isNew())
            user.setCreateDate(new Date());
        return userRepository.save(user);
    }

    /**
     * Deletes the user.
     *
     * @param user the user to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(U user) {
        userRepository.delete(user);
    }

    /*
    private U getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        U user = userRepository.findFirstByUsername(auth.getName());
        System.out.println(user.getFirstName());
        return user;
    }
    */

}