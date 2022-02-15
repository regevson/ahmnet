package at.qe.skeleton.dtos;

import java.util.ArrayList;
import java.util.Collection;

import at.qe.skeleton.model.User;

public class UserMapper {

    public static UserDto mapToUserDto(User user) {
	UserDto dto = new UserDto();
	dto.setId(user.getId());
	dto.setFirstName(user.getFirstName());
	dto.setLastName(user.getLastName());
	dto.setFullName(user.getFirstName() + " " + user.getLastName());
	dto.setClubName(user.getClub().getName());
	return dto;
    }
    public static Collection<UserDto> mapToUserDto(Collection<User> users) {
	Collection<UserDto> dtos = new ArrayList<>();
	for(User u : users)
	    dtos.add(mapToUserDto(u));
	return dtos;
    }

}
