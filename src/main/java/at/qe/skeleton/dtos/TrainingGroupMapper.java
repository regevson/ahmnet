package at.qe.skeleton.dtos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.qe.skeleton.model.TrainingGroup;
import at.qe.skeleton.model.User;
import at.qe.skeleton.services.UserService;

@Component
public class TrainingGroupMapper {

    @Autowired
    UserService userService;

    public TrainingGroupDto mapToTrainingGroupDto(TrainingGroup trg) {
	TrainingGroupDto dto = new TrainingGroupDto();
	dto.setId(trg.getId());
	dto.setTrainer(UserMapper.mapToUserDto(trg.getTrainer()));
	dto.setClub(trg.getClub());
	dto.setPlayers(UserMapper.mapToUserDto(trg.getPlayers()));
	dto.setNumRemainingSessions(trg.getNumRemainingSessions());
	return dto;
    }
    public Collection<TrainingGroupDto> mapToTrainingGroupDto(Collection<TrainingGroup> groups) {
	Collection<TrainingGroupDto> dtos = new ArrayList<>();
	for(TrainingGroup tg : groups)
	    dtos.add(mapToTrainingGroupDto(tg));
	return dtos;
    }

    public void mapFromTrainingGroupDto(TrainingGroupDto dto, TrainingGroup trg) {
	trg.setTrainer(this.userService.loadUser(dto.getTrainer().getId()));
	trg.setClub(dto.getClub());
        Set<User> players = dto.getPlayers().stream().map(p -> userService.loadUser(p.getId())).collect(Collectors.toSet());
	trg.setPlayers(players);
	trg.setNumRemainingSessions(dto.getNumRemainingSessions());
    }

}
