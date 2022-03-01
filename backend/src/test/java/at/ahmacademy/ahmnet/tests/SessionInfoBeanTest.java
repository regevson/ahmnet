package at.ahmacademy.ahmnet.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;

import at.ahmacademy.ahmnet.model.UserRole;
import at.ahmacademy.ahmnet.services.UserService;
import at.ahmacademy.ahmnet.ui.beans.SessionInfoBean;

/**
 * Some very basic tests for {@link UserService}.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@SpringBootTest
@WebAppConfiguration
public class SessionInfoBeanTest {

    @Autowired
    SessionInfoBean sessionInfoBean;

    @Autowired
    UserService userService;

    @Test
    @WithMockUser(username = "johndoe", authorities = {"TRAINER"})
    public void testLoggedIn() {
        Assertions.assertTrue(sessionInfoBean.isLoggedIn(), "sessionInfoBean.isLoggedIn does not return true for authenticated user");
        Assertions.assertEquals("johndoe", sessionInfoBean.getCurrentUserName(), "sessionInfoBean.getCurrentUserName does not return authenticated user's name");
        Assertions.assertEquals("johndoe", sessionInfoBean.getCurrentUser().getUsername(), "sessionInfoBean.getCurrentUser does not return authenticated user");
        Assertions.assertEquals("TRAINER", sessionInfoBean.getCurrentUserRoles(), "sessionInfoBean.getCurrentUserRoles does not return authenticated user's roles");
        Assertions.assertTrue(sessionInfoBean.hasRole("TRAINER"), "sessionInfoBean.hasRole does not return true for a role the authenticated user has");
        Assertions.assertFalse(sessionInfoBean.hasRole("ADMIN"), "sessionInfoBean.hasRole does not return false for a role the authenticated user does not have");
    }

    @Test
    public void testNotLoggedIn() {
        Assertions.assertFalse(sessionInfoBean.isLoggedIn(), "sessionInfoBean.isLoggedIn does return true for not authenticated user");
        Assertions.assertEquals("", sessionInfoBean.getCurrentUserName(), "sessionInfoBean.getCurrentUserName does not return empty string when not logged in");
        Assertions.assertNull(sessionInfoBean.getCurrentUser(), "sessionInfoBean.getCurrentUser does not return null when not logged in");
        Assertions.assertEquals("", sessionInfoBean.getCurrentUserRoles(), "sessionInfoBean.getCurrentUserRoles does not return empty string when not logged in");
        for (UserRole role : UserRole.values()) {
            Assertions.assertFalse(sessionInfoBean.hasRole(role.name()), "sessionInfoBean.hasRole does not return false for all possible roales");
        }
    }

}