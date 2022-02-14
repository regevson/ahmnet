package at.qe.skeleton.dtos;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.qe.skeleton.model.Training;
import at.qe.skeleton.model.User;
import at.qe.skeleton.services.TrainingGroupService;
import at.qe.skeleton.services.TrainingService;
import at.qe.skeleton.services.UserService;

@Component
public class TrainingMapper {

    @Autowired
    TrainingService trainingService;
    @Autowired
    UserService userService;
    @Autowired
    TrainingGroupMapper groupMapper;
    @Autowired
    TrainingGroupService trainingGroupService;

    public TrainingTimeslotDto mapToTrainingTimeslotDto(Training tr) {
	TrainingTimeslotDto dto = new TrainingTimeslotDto();
	dto.setId(tr.getId());
	dto.setClub(tr.getClub());
	dto.setDate(trainingService.convertDateToGerman(tr.getDateTime().toLocalDate()));
	LocalTime startTime = tr.getDateTime().toLocalTime();
	dto.setTimeslot(startTime.toString() + " - " + startTime.plusMinutes(tr.getDurationMinutes()).toString());
	dto.setStartTime(startTime);
	dto.setDurationMinutes(tr.getDurationMinutes());
	dto.setCourt(tr.getCourt());
	return dto;
    }
    public Collection<TrainingTimeslotDto> mapToUserDto(Collection<Training> trainings) {
	Collection<TrainingTimeslotDto> dtos = new ArrayList<>();
	for(Training t : trainings)
	    dtos.add(mapToTrainingTimeslotDto(t));
	return dtos;
    }

    public TrainingDetailsDto mapToTrainingDetailsDto(Training tr) {
	TrainingDetailsDto dto = new TrainingDetailsDto();
	dto.setId(tr.getId());
	dto.setGroup(groupMapper.mapToTrainingGroupDto(tr.getTrainingGroup()));
	dto.setClub(tr.getClub());
	dto.setCourt(tr.getCourt());
	dto.setDate(tr.getDateTime().toLocalDate());
	LocalTime startTime = tr.getDateTime().toLocalTime();
	dto.setStartTime(startTime);
	dto.setTimeslot(startTime.toString() + " - " + startTime.plusMinutes(tr.getDurationMinutes()).toString());
	dto.setDurationMinutes(tr.getDurationMinutes());
	dto.setTrainer(UserMapper.mapToUserDto(tr.getTrainer()));
	dto.setPlayers(UserMapper.mapToUserDto(tr.getTrainingGroup().getPlayers()));
	dto.setAttendees(tr.getAttendees().stream().map(u -> u.getId()).collect(Collectors.toList()));
	dto.setBulletPoints(tr.getBulletPoints());
	dto.setComments(tr.getComment());
	dto.setIsFree(tr.isFree());
	return dto;
    }
    
    public void mapFromTrainingDetailsDto(TrainingDetailsDto dto, Training tr) {
        tr.setTrainingGroup(trainingGroupService.loadTrainingGroupById(dto.getGroup().getId()));
        tr.setTrainer(userService.loadUser(dto.getTrainer().getId()));
        tr.setClub(dto.getClub());
	tr.setCourt(dto.getCourt());
        tr.setDateTime(dto.getDate().atTime(dto.getStartTime()));
        tr.setDurationMinutes(dto.getDurationMinutes());
        //List<User> players = dto.getPlayers().stream().map(p -> userService.loadUser(p.getId())).collect(Collectors.toList());
        if(dto.getAttendees() != null)
            tr.setAttendees(dto.getAttendees().stream().map(id -> userService.loadUser(id)).collect(Collectors.toSet()));
        tr.setBulletPoints(dto.getBulletPoints());
        tr.setComment(dto.getComments());
        tr.setFree(dto.getIsFree());
    }
}
