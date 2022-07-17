package at.ahmacademy.ahmnet.dtos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.ahmacademy.ahmnet.model.User;

@Component
public class UserMapper {

  public UserDto mapToDto(User user) {
    UserDto dto = new UserDto();
    dto.setId(user.getId());
    dto.setFirstName(user.getFirstName());
    dto.setLastName(user.getLastName());
    dto.setFullName(user.getFirstName() + " " + user.getLastName());
    dto.setBirthYear(user.getBirthYear());
    dto.setClubId(user.getClub().getId());
    dto.setTrainingGroupIds(user.getTrainingGroups().stream().map(g -> g.getId()).collect(Collectors.toSet()));
    dto.setRoles(user.getRoles());

    String groupClubIds = user.getTrainingGroups().stream().map(a -> a.getClub().getId()).collect(Collectors.joining(","));
    String groupIds = user.getTrainingGroups().stream().map(a -> Long.toString(a.getId())).collect(Collectors.joining(","));
    dto.setGroups_url("clubs/" + groupClubIds + "/groups/" + groupIds);

    return dto;
  }
  public Collection<UserDto> mapToDto(Collection<User> users) {
    Collection<UserDto> dtos = new ArrayList<>();
    for(User u : users)
      dtos.add(mapToDto(u));
    return dtos;
  }

}
