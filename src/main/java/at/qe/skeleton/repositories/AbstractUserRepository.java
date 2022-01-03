package at.qe.skeleton.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import at.qe.skeleton.model.AbstractUser;

@NoRepositoryBean
public interface AbstractUserRepository<U extends AbstractUser> extends AbstractRepository<U,String> {

    U findFirstByUsername(String username);

    List<U> findByUsernameContaining(String username);

    /*
    @Query("SELECT u FROM User u WHERE CONCAT(u.firstName, ' ', u.lastName) = :wholeName")
    List<U> findByWholeNameConcat(@Param("wholeName") String wholeName);

    @Query("SELECT u FROM User u WHERE :role MEMBER OF u.roles")
    List<U> findByRole(@Param("role") UserRole role);
    */

}