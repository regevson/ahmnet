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
	return dto;
    }
    public static Collection<UserDto> mapToUserDto(Collection<User> user) {
	Collection<UserDto> dtos = new ArrayList<>();
	for(User u : user)
	    dtos.add(mapToUserDto(u));
	return dtos;
    }

}
