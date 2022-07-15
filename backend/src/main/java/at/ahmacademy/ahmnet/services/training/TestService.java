package at.ahmacademy.ahmnet.services.training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import at.ahmacademy.ahmnet.model.TestClass;
import at.ahmacademy.ahmnet.model.TrainingGroup;
import at.ahmacademy.ahmnet.repositories.TestRepository;
import at.ahmacademy.ahmnet.services.trainingGroup.TrainingGroupService;

@Service
@Scope("application")
public class TestService {

  @Autowired
  private TestRepository testRepo;
  @Autowired
  private TrainingGroupService ser;


  public TestClass saveTraining(TestClass training) {
    return testRepo.save(training);
  }

  public void deleteTraining(TestClass t) {
    //System.out.println("deleting id " + t.getId());
    //TrainingGroup g = t.getTrainingGroup();
    //g.setTrainings(null);
    //ser.saveGroup(g);
    //t.setTrainingGroup(null);
    testRepo.delete(t);
  }

  public TestClass loadTrainingById(Long id) {
    return testRepo.findFirstById(id);
  }


}
