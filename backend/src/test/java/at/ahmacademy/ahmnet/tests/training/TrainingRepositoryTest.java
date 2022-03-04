package at.ahmacademy.ahmnet.tests.training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import at.ahmacademy.ahmnet.repositories.TrainingRepository;

@SpringBootTest
@WebAppConfiguration
public class TrainingRepositoryTest {

  @Autowired
  TrainingRepository trainingRepo;

}
