package at.ahmacademy.ahmnet.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.model.UserRole;

public interface UserRepository extends AbstractRepository<User, String> {

  User findFirstByUsername(String username);

  List<User> findByUsernameContaining(String username);

  @Query("SELECT u FROM User u WHERE CONCAT(u.firstName, ' ', u.lastName) = :wholeName")
  List<User> findByWholeNameConcat(@Param("wholeName") String wholeName);

  @Query("SELECT u FROM User u WHERE :role MEMBER OF u.roles")
  List<User> findByRole(@Param("role") UserRole role);

  @Query("SELECT u FROM User u WHERE u.id in :ids")
  Set<User> getUsersById(@Param("ids") String ids[]);

  Set<User> findByClub_NameContaining(String clubName);

}
