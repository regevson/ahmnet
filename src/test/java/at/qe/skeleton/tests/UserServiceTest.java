package at.qe.skeleton.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import at.qe.skeleton.model.AbstractUser;
import at.qe.skeleton.model.Client;
import at.qe.skeleton.model.Trainer;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.services.AbstractUserService;
import at.qe.skeleton.services.ClientService;
import at.qe.skeleton.services.TrainerService;
import at.qe.skeleton.services.UserService;

/**
 * Some very basic tests for {@link UserService}.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@SpringBootTest
@WebAppConfiguration
public class UserServiceTest {

    @Autowired
    TrainerService trainerService;

    @Autowired
    ClientService clientService;

    int trainerNum = 2;
    int clientNum = 1;

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDatainitialization() {
        this.<Trainer,TrainerService>testDatainitializationHelper(this.trainerService, trainerNum);
        this.<Client,ClientService>testDatainitializationHelper(this.clientService, clientNum);
    }

    private <U extends AbstractUser,S extends AbstractUserService<U>> void testDatainitializationHelper(S service, int numUsers) {
        Assertions.assertEquals(numUsers, service.getAllUsers().size(), "Insufficient amount of users initialized for test data source");
        for (U user : service.getAllUsers()) {
            if ("danihuber".equals(user.getUsername())) {
                Assertions.assertTrue(user.getRoles().contains(UserRole.ADMIN), "User \"" + user + "\" does not have role ADMIN");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
            } 
            else if ("mandyflores".equals(user.getUsername())) {
                Assertions.assertTrue(user.getRoles().contains(UserRole.EMPLOYEE), "User \"" + user + "\" does not have role MANAGER");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
            } 
            else if ("regevson".equals(user.getUsername())) {
                Assertions.assertTrue(user.getRoles().contains(UserRole.EMPLOYEE), "User \"" + user + "\" does not have role MANAGER");
                Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
            } 
            else
                Assertions.fail("Unknown user \"" + user.getUsername() + "\" loaded from test data source via UserService.getAllUsers");
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDeleteUser() {
        this.<Trainer,TrainerService>testDeleteUserHelper(this.trainerService, trainerNum, "regevson");
        this.<Client,ClientService>testDeleteUserHelper(this.clientService, clientNum, "mandyflores");
    }

    private <U extends AbstractUser,S extends AbstractUserService<U>> void testDeleteUserHelper(S service, int numUsers, String username) {
        U toBeDeletedUser = service.loadUser(username);
        Assertions.assertNotNull(toBeDeletedUser, "User \"" + username + "\" could not be loaded from test data source");

        service.deleteUser(toBeDeletedUser);

        Assertions.assertEquals(numUsers-1, service.getAllUsers().size(), "No user has been deleted after calling UserService.deleteUser");
        U deletedUser = service.loadUser(username);
        Assertions.assertNull(deletedUser, "Deleted User \"" + username + "\" could still be loaded from test data source via UserService.loadUser");

        for (U remainingUser : service.getAllUsers()) {
            Assertions.assertNotEquals(toBeDeletedUser.getUsername(), remainingUser.getUsername(), "Deleted User \"" + username + "\" could still be loaded from test data source via UserService.getAllUsers");
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpdateUser() {
        this.<Trainer,TrainerService>testUpdateUserHelper(this.trainerService, "regevson");
        this.<Client,ClientService>testUpdateUserHelper(this.clientService, "mandyflores");
    }

    private <U extends AbstractUser,S extends AbstractUserService<U>> void testUpdateUserHelper(S service, String username) {
        U toBeSavedUser = service.loadUser(username);
        Assertions.assertNotNull(toBeSavedUser, "User \"" + username + "\" could not be loaded from test data source");

        toBeSavedUser.setEmail("changed-email@whatever.wherever");
        service.saveUser(toBeSavedUser);

        U freshlyLoadedUser = service.loadUser(username);
        Assertions.assertNotNull(freshlyLoadedUser, "User \"" + username + "\" could not be loaded from test data source after being saved");
        Assertions.assertEquals("changed-email@whatever.wherever", freshlyLoadedUser.getEmail(), "User \"" + username + "\" does not have a the correct email attribute stored being saved");
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreateUser() {
        this.<Trainer,TrainerService>testCreateUserHelper(this.trainerService, new Trainer());
        this.<Client,ClientService>testCreateUserHelper(this.clientService, new Client());
    }

    private <U extends AbstractUser,S extends AbstractUserService<U>> void testCreateUserHelper(S service, U toBeCreatedUser) {
        String username = "newuser";
        String password = "passwd";
        String fName = "New";
        String lName = "User";
        String email = "new-email@whatever.wherever";
        String phone = "+12 345 67890";
        toBeCreatedUser.setUsername(username);
        toBeCreatedUser.setPassword(password);
        toBeCreatedUser.setEnabled(true);
        toBeCreatedUser.setFirstName(fName);
        toBeCreatedUser.setLastName(lName);
        toBeCreatedUser.setEmail(email);
        toBeCreatedUser.setPhone(phone);
        toBeCreatedUser.setRoles(Sets.newSet(UserRole.EMPLOYEE, UserRole.MANAGER));
        service.saveUser(toBeCreatedUser);

        U freshlyCreatedUser = service.loadUser(username);
        Assertions.assertNotNull(freshlyCreatedUser, "New user could not be loaded from test data source after being saved");
        Assertions.assertEquals(username, freshlyCreatedUser.getUsername(), "New user could not be loaded from test data source after being saved");
        Assertions.assertEquals(password, freshlyCreatedUser.getPassword(), "User \"" + username + "\" does not have a the correct password attribute stored being saved");
        Assertions.assertEquals(fName, freshlyCreatedUser.getFirstName(), "User \"" + username + "\" does not have a the correct firstName attribute stored being saved");
        Assertions.assertEquals(lName, freshlyCreatedUser.getLastName(), "User \"" + username + "\" does not have a the correct lastName attribute stored being saved");
        Assertions.assertEquals(email, freshlyCreatedUser.getEmail(), "User \"" + username + "\" does not have a the correct email attribute stored being saved");
        Assertions.assertEquals(phone, freshlyCreatedUser.getPhone(), "User \"" + username + "\" does not have a the correct phone attribute stored being saved");
        Assertions.assertTrue(freshlyCreatedUser.getRoles().contains(UserRole.MANAGER), "User \"" + username + "\" does not have role MANAGER");
        Assertions.assertTrue(freshlyCreatedUser.getRoles().contains(UserRole.EMPLOYEE), "User \"" + username + "\" does not have role EMPLOYEE");
        Assertions.assertNotNull(freshlyCreatedUser.getCreateDate(), "User \"" + username + "\" does not have a createDate defined after being saved");
    }

/*
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testExceptionForEmptyUsername() {
        Assertions.assertThrows(org.springframework.orm.jpa.JpaSystemException.class, () -> {
            User adminUser = userService.loadUser("admin");
            Assertions.assertNotNull(adminUser, "Admin user could not be loaded from test data source");

            User toBeCreatedUser = new User();
            userService.saveUser(toBeCreatedUser);
        });
    }

    @Test
    public void testUnauthenticateddLoadUsers() {
        Assertions.assertThrows(org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class, () -> {
            for (User user : userService.getAllUsers()) {
                Assertions.fail("Call to userService.getAllUsers should not work without proper authorization");
            }
        });
    }

    @Test
    @WithMockUser(username = "user", authorities = {"EMPLOYEE"})
    public void testUnauthorizedLoadUsers() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            for (User user : userService.getAllUsers()) {
                Assertions.fail("Call to userService.getAllUsers should not work without proper authorization");
            }
        });
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedLoadUser() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            User user = userService.loadUser("admin");
            Assertions.fail("Call to userService.loadUser should not work without proper authorization for other users than the authenticated one");
        });
    }

    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testAuthorizedLoadUser() {
        String username = "user1";
        User user = userService.loadUser(username);
        Assertions.assertEquals(username, user.getUsername(), "Call to userService.loadUser returned wrong user");
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedSaveUser() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            String username = "user1";
            User user = userService.loadUser(username);
            Assertions.assertEquals(username, user.getUsername(), "Call to userService.loadUser returned wrong user");
            userService.saveUser(user);
        });
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedDeleteUser() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            User user = userService.loadUser("user1");
            Assertions.assertEquals("user1", user.getUsername(), "Call to userService.loadUser returned wrong user");
            userService.deleteUser(user);
        });
    }
    */

}