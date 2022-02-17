package at.qe.skeleton.dtos;

import java.util.Set;

import at.qe.skeleton.model.UserRole;
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
    private String clubName;
    private Set<UserRole> roles;

}
