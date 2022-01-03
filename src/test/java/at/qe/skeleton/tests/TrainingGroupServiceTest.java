package at.qe.skeleton.tests;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import at.qe.skeleton.model.AbstractUser;
import at.qe.skeleton.model.Trainer;
import at.qe.skeleton.model.TrainingGroup;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.repositories.AbstractUserRepository;
import at.qe.skeleton.services.AbstractUserService;
import at.qe.skeleton.services.TrainerService;
import at.qe.skeleton.services.TrainingGroupService;

@SpringBootTest
@WebAppConfiguration
public class TrainingGroupServiceTest {

    @Autowired
    TrainingGroupService trainingGroupService;

    @Autowired
    TrainerService trainerService;

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetGroupsByTrainer() {
        Trainer trainer = trainerService.loadUser("danihuber");
        Set<TrainingGroup> groups = this.trainingGroupService.loadTrainingGroupByTrainer(trainer);
        
    }

    private <U extends AbstractUser,R extends AbstractUserRepository<U>,S extends AbstractUserService<U, R>> void testDatainitializationHelper(S service, int numUsers) {
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


}