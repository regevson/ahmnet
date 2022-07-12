package at.ahmacademy.ahmnet.dtos;

import java.util.Collection;

import at.ahmacademy.ahmnet.model.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {

  private String id;
  private String firstName;
  private String lastName;
  private String fullName;
  private Integer birthYear;
  private String clubId;
  private Collection<Long> trainingGroupIds;
  private Collection<UserRole> roles;

  private String groups_url;

}

/*
*/
