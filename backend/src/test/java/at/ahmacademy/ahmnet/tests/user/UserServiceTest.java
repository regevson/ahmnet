package at.ahmacademy.ahmnet.tests.user;

import static org.mockito.Mockito.doReturn;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.ahmacademy.ahmnet.model.Club;
import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.model.UserRole;
import at.ahmacademy.ahmnet.repositories.UserRepository;
import at.ahmacademy.ahmnet.services.user.UserService;

@SpringBootTest
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class UserServiceTest {

  @Autowired
  UserRepository userRepo;

  UserService userService;
  @BeforeEach
  public void initTrainingService() {
    userService = new UserService();
    userService.setUserRepository(userRepo);
  }

  @Test
  @WithMockUser(username = "admin", authorities = {"ADMIN"})
  public void testDatainitialization() {
    Assertions.assertTrue(userService.getAllUsers().size() > 2, "Insufficient amount of users initialized for test data source");
    for (User user: userService.getAllUsers()) {
      if ("admin".equals(user.getUsername())) {
        Assertions.assertTrue(user.getRoles().contains(UserRole.ADMIN), "User \"" + user + "\" does not have role ADMIN");
        Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
        Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
        Assertions.assertNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
        Assertions.assertNull(user.getUpdateDate(), "User \"" + user + "\" has a updateDate defined");
      } else if ("susi".equals(user.getUsername())) {
        Assertions.assertTrue(user.getRoles().contains(UserRole.PLAYER), "User \"" + user + "\" does not have role PLAYER");
        Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
        Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
        Assertions.assertNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
        Assertions.assertNull(user.getUpdateDate(), "User \"" + user + "\" has a updateDate defined");
      } else if ("max".equals(user.getUsername())) {
        Assertions.assertTrue(user.getRoles().contains(UserRole.PLAYER), "User \"" + user + "\" does not have role TRAINER");
        Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
        Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
        Assertions.assertNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
        Assertions.assertNull(user.getUpdateDate(), "User \"" + user + "\" has a updateDate defined");
      } else if ("johndoe".equals(user.getUsername())) {
        Assertions.assertTrue(user.getRoles().contains(UserRole.TRAINER), "User \"" + user + "\" does not have role ADMIN");
        Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
        Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
        Assertions.assertNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
        Assertions.assertNull(user.getUpdateDate(), "User \"" + user + "\" has a updateDate defined");
      } else if ("jure".equals(user.getUsername())) {
        Assertions.assertTrue(user.getRoles().contains(UserRole.TRAINER), "User \"" + user + "\" does not have role ADMIN");
        Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
        Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
        Assertions.assertNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
        Assertions.assertNull(user.getUpdateDate(), "User \"" + user + "\" has a updateDate defined");
      } else if ("mare".equals(user.getUsername())) {
        Assertions.assertTrue(user.getRoles().contains(UserRole.TRAINER), "User \"" + user + "\" does not have role ADMIN");
        Assertions.assertNotNull(user.getCreateUser(), "User \"" + user + "\" does not have a createUser defined");
        Assertions.assertNotNull(user.getCreateDate(), "User \"" + user + "\" does not have a createDate defined");
        Assertions.assertNull(user.getUpdateUser(), "User \"" + user + "\" has a updateUser defined");
        Assertions.assertNull(user.getUpdateDate(), "User \"" + user + "\" has a updateDate defined");
      } else {
        Assertions.fail("Unknown user \"" + user.getUsername() + "\" loaded from test data source via UserService.getAllUsers");
      }
    }
  }

  @Disabled
  @DirtiesContext
  @Test
  @WithMockUser(username = "admin", authorities = {"ADMIN"})
  public void testDeleteUser() {
    String username = "johndoe";
    User adminUser = userService.loadUser("admin");
    Assertions.assertNotNull(adminUser, "Admin user could not be loaded from test data source");
    User toBeDeletedUser = userService.loadUser(username);
    Assertions.assertNotNull(toBeDeletedUser, "User \"" + username + "\" could not be loaded from test data source");

    userService.deleteUser(toBeDeletedUser);

    Assertions.assertEquals(3, userService.getAllUsers().size(), "No user has been deleted after calling UserService.deleteUser");
    User deletedUser = userService.loadUser(username);
    Assertions.assertNull(deletedUser, "Deleted User \"" + username + "\" could still be loaded from test data source via UserService.loadUser");

    for (User remainingUser: userService.getAllUsers()) {
      Assertions.assertNotEquals(toBeDeletedUser.getUsername(), remainingUser.getUsername(), "Deleted User \"" + username + "\" could still be loaded from test data source via UserService.getAllUsers");
    }
  }

  @DirtiesContext
  @Test
  @WithMockUser(username = "admin", authorities = {"ADMIN"})
  public void testUpdateUser() {
    String username = "susi";
    User adminUser = userService.loadUser("admin");
    Assertions.assertNotNull(adminUser, "Admin user could not be loaded from test data source");
    User toBeSavedUser = userService.loadUser(username);
    Assertions.assertNotNull(toBeSavedUser, "User \"" + username + "\" could not be loaded from test data source");

    Assertions.assertNull(toBeSavedUser.getUpdateUser(), "User \"" + username + "\" has a updateUser defined");
    Assertions.assertNull(toBeSavedUser.getUpdateDate(), "User \"" + username + "\" has a updateDate defined");

    toBeSavedUser.setEmail("changed-email@whatever.wherever");
    userService.saveUser(toBeSavedUser);

    User freshlyLoadedUser = userService.loadUser("susi");
    Assertions.assertNotNull(freshlyLoadedUser, "User \"" + username + "\" could not be loaded from test data source after being saved");
    Assertions.assertNotNull(freshlyLoadedUser.getUpdateUser(), "User \"" + username + "\" does not have a updateUser defined after being saved");
    Assertions.assertEquals(adminUser, freshlyLoadedUser.getUpdateUser(), "User \"" + username + "\" has wrong updateUser set");
    Assertions.assertNotNull(freshlyLoadedUser.getUpdateDate(), "User \"" + username + "\" does not have a updateDate defined after being saved");
    Assertions.assertEquals("changed-email@whatever.wherever", freshlyLoadedUser.getEmail(), "User \"" + username + "\" does not have a the correct email attribute stored being saved");
  }

  @Transactional
  @DirtiesContext
  @Test
  @WithMockUser(username = "admin", authorities = {"ADMIN"})
  public void testCreateUser() {
    User adminUser = userService.loadUser("admin");
    Assertions.assertNotNull(adminUser, "Admin user could not be loaded from test data source");

    String password = "passwd";
    String fName = "New";
    String lName = "User";
    String email = "new-email@whatever.wherever";
    String phone = "+12 345 67890";
    User toBeCreatedUser = new User();
    toBeCreatedUser.setPassword(password);
    toBeCreatedUser.setEnabled(true);
    toBeCreatedUser.setFirstName(fName);
    toBeCreatedUser.setLastName(lName);
    toBeCreatedUser.setEmail(email);
    toBeCreatedUser.setPhone(phone);
    toBeCreatedUser.setRoles(Set.of(UserRole.PLAYER));
    userService.saveUser(toBeCreatedUser);

    String username = fName + lName;
    User freshlyCreatedUser = userService.loadUser(username);
    Assertions.assertNotNull(freshlyCreatedUser, "New user could not be loaded from test data source after being saved");
    Assertions.assertEquals(username, freshlyCreatedUser.getUsername(), "New user could not be loaded from test data source after being saved");
    Assertions.assertEquals(password, freshlyCreatedUser.getPassword(), "User \"" + username + "\" does not have a the correct password attribute stored being saved");
    Assertions.assertEquals(fName, freshlyCreatedUser.getFirstName(), "User \"" + username + "\" does not have a the correct firstName attribute stored being saved");
    Assertions.assertEquals(lName, freshlyCreatedUser.getLastName(), "User \"" + username + "\" does not have a the correct lastName attribute stored being saved");
    Assertions.assertEquals(email, freshlyCreatedUser.getEmail(), "User \"" + username + "\" does not have a the correct email attribute stored being saved");
    Assertions.assertEquals(phone, freshlyCreatedUser.getPhone(), "User \"" + username + "\" does not have a the correct phone attribute stored being saved");
    Assertions.assertTrue(freshlyCreatedUser.getRoles().contains(UserRole.PLAYER), "User \"" + username + "\" does not have role PLAYER");
    Assertions.assertNotNull(freshlyCreatedUser.getCreateUser(), "User \"" + username + "\" does not have a createUser defined after being saved");
    Assertions.assertEquals(adminUser, freshlyCreatedUser.getCreateUser(), "User \"" + username + "\" has wrong createUser set");
    Assertions.assertNotNull(freshlyCreatedUser.getCreateDate(), "User \"" + username + "\" does not have a createDate defined after being saved");
  }

  @Disabled
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

  @Disabled
  @Test
  public void testUnauthenticatedLoadUsers() {
    Assertions.assertThrows(org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class, () -> {
      for (User user: userService.getAllUsers()) {
        Assertions.fail("Call to userService.getAllUsers should not work without proper authorization");
      }
    });
  }

  @Disabled
  @Test
  @WithMockUser(username = "user", authorities = {"TRAINER"})
  public void testUnauthorizedLoadUsers() {
    Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
      for (User user: userService.getAllUsers()) {
        Assertions.fail("Call to userService.getAllUsers should not work without proper authorization");
      }
    });
  }

  @Disabled
  @Test
  @WithMockUser(username = "susi", authorities = {"PLAYER"})
  public void testUnauthorizedLoadUser() {
  
    UserRepository fakeUsrRepo = Mockito.mock(UserRepository.class);
    doReturn(null).when(fakeUsrRepo).findFirstByUsername(Mockito.anyString());

    UserService ser = Mockito.mock(UserService.class);
    ser.setUserRepository(fakeUsrRepo);

    User u = User.builder().username("malcolm").build();
    doReturn(u).when(ser).getAuthUser();
    Mockito.when(ser.loadUser(Mockito.anyString())).thenCallRealMethod();

    Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
      ser.loadUser("testuser");
    });
  }

  @WithMockUser(username = "susi", authorities = {"TRAINER"})
  public void testAuthorizedLoadUser() {
    String username = "susi";
    User user = userService.loadUser(username);
    Assertions.assertEquals(username, user.getUsername(), "Call to userService.loadUser returned wrong user");
  }

  @Disabled
  @Test
  @WithMockUser(username = "susi", authorities = {"PLAYER"})
  public void testUnauthorizedSaveUser() {
    Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
      User user = User.builder().username("newuser").build();
      userService.saveUser(user);
    });
  }

  @Disabled
  @Transactional
  @Test
  @WithMockUser(username = "mare", authorities = {"TRAINER"})
  public void testUnauthorizedDeleteUser() {
    Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
      User user = userService.loadUser("max");
      Assertions.assertEquals("max", user.getUsername(), "Call to userService.loadUser returned wrong user");
      userService.deleteUser(user);
    });
  }
  
  @Test
  public void testLoadUsersByClubAndRole() {
    UserRepository fakeUsrRepo = Mockito.mock(UserRepository.class);
    userService.setUserRepository(fakeUsrRepo);
    
    Club club1 = Club.builder().id("TC-Wiesing").build();
    Club club2 = Club.builder().id("TC-Terfens").build();

    User p1 = User.builder().username("p1").club(club1).roles(Set.of(UserRole.PLAYER)).build();
    User p2 = User.builder().username("p2").club(club1).roles(Set.of(UserRole.ADMIN)).build();
    User p3 = User.builder().username("p3").club(club2).roles(Set.of(UserRole.PLAYER)).build();

    doReturn(Set.of(p1,p2)).when(fakeUsrRepo).findByClub_IdContaining(Mockito.anyString());

    Set<User> users = userService.loadUsersByClubAndRole(club1.getId(), Optional.of(UserRole.PLAYER));
    Assertions.assertEquals(1, users.size(), "wrong numbear of users retrieved");
    Assertions.assertEquals("p1", users.iterator().next().getId(), "wrong users retrieved");
  }

  @Test
  public void testCreateUserName() {
    UserRepository fakeUsrRepo = Mockito.mock(UserRepository.class);
    userService.setUserRepository(fakeUsrRepo);
    
    User u = User.builder().firstName("firstName").lastName("lastName").createDate(null).build();

    doReturn(u).when(fakeUsrRepo).save(Mockito.any());
    doReturn(u).when(fakeUsrRepo).findFirstByUsername(u.getFirstName() + u.getLastName());

    u = userService.saveUser(u);
    Assertions.assertEquals("firstNamelastName1", u.getId(), "wrong id created");
  }
  
  
  

}
