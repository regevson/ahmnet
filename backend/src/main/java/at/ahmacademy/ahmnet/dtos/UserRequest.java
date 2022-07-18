package at.ahmacademy.ahmnet.dtos;

import java.util.Set;

import at.ahmacademy.ahmnet.model.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserRequest {

  private String firstName;
  private String lastName;
  private Integer birthYear;
  private String email;
  private String phone;
  private String clubId;
  private Set<UserRole> roles;

}
