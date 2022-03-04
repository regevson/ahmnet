package at.ahmacademy.ahmnet.services.training;

import static at.ahmacademy.ahmnet.model.UserRole.ADMIN;
import static at.ahmacademy.ahmnet.model.UserRole.TRAINER;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Scope;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.model.UserRole;

@Service
@Scope("application")
public class TrainingAuthService implements PermissionEvaluator {

  @SuppressWarnings("unchecked")
  @Override
  public boolean hasPermission(Authentication auth, Object obj, Object perm) {
    if((auth == null) || (obj == null) || !(obj instanceof List) || !(perm instanceof String))
      return false;

    String userId = auth.getName();
    List<Training> trainings = (List<Training>) obj;
    Set<UserRole> roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                                                        .map(UserRole::valueOf)
                                                        .collect(Collectors.toSet());
    Predicate<Training> pred = null;
    if(perm.toString().equals("free"))
      pred = isAdmin(roles).or(isTrainerOfTraining(userId));
    else if(perm.toString().equals("grab"))
      pred = isAdmin(roles).or( isTrainer(roles).and(isFree()) );
    
    return trainings.stream().allMatch(pred::test);
  }

  public Predicate<Training> isTrainerOfTraining(String userId) {
    return (training) -> userId.equals(training.getTrainer().getId());
  }

  public Predicate<Training> isFree() {
    return (training) -> training.getIsFree();
  }

  public Predicate<Training> isAdmin(Set<UserRole> roles) {
    return (training) -> roles.contains(ADMIN);
  }

  public Predicate<Training> isTrainer(Set<UserRole> roles) {
    return (training) -> roles.contains(TRAINER);
  }







  @Override
  public boolean hasPermission(Authentication auth, Serializable obj, String type, Object perm) {
    return false;
  }

}
