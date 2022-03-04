package at.ahmacademy.ahmnet.repositories;

import org.springframework.data.jpa.domain.Specification;

import at.ahmacademy.ahmnet.model.Training;

public class TrainingSpecification {

  public static Specification<Training> hasTrainer(String trainerId) {
    return (root, query, builder) -> builder.like(root.get("trainer").get("username"), trainerId);
  }

  public static Specification<Training> hasFreeStatus(Boolean isFree) {
    return (root, query, builder) -> builder.equal(root.get("isFree"), isFree);
  }

  public static Specification<Training> hasWeekNum(int weekNum) {
    return (root, query, builder) -> builder.equal(root.get("weekNum"), weekNum);
  }

  public static Specification<Training> exclId(String exclTrainerId) {
    return (root, query, builder) 
                  -> builder.notLike(root.get("trainer").get("username"), exclTrainerId);
  }

}
