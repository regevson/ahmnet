package at.qe.skeleton.ui.controllers;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.qe.skeleton.dtos.TrainingGroupDto;
import at.qe.skeleton.dtos.TrainingGroupMapper;
import at.qe.skeleton.dtos.TrainingGroupSnippetDto;
import at.qe.skeleton.model.Club;
import at.qe.skeleton.model.TrainingGroup;
import at.qe.skeleton.services.TrainingGroupService;

@RequestMapping("/api")
@RestController
@Scope("application")
public class TrainingGroupController {

    @Autowired
    TrainingGroupService trainingGroupService;
    @Autowired
    TrainingGroupMapper trainingGroupMapper;

    @GetMapping("/newGroup")
    public TrainingGroupDto getNewGroup() {
	TrainingGroupDto dto = new TrainingGroupDto();
	dto.setId(-1L);
	return dto;
    }

    @GetMapping("/allClubs")
    public Collection<Club> getAllClubs() {
	return this.trainingGroupService.loadAllClubs();
    }

    @GetMapping("/allGroups")
    public Collection<TrainingGroupSnippetDto> getAllGroups() {
	return this.trainingGroupMapper.mapToTrainingGroupSnippetDto(trainingGroupService.loadAllGroups());
    }
    
    @GetMapping("/deleteGroup")
    public void deleteGroup(long id) {
    try {
	this.trainingGroupService.deleteGroup(id);
    }catch(Exception e) { e.printStackTrace();};
    }

    @GetMapping("/groupsByClub")
    public Collection<TrainingGroupSnippetDto> getGroupsByClub(String clubName) {
        return this.trainingGroupMapper.mapToTrainingGroupSnippetDto(trainingGroupService.loadTrainingGroupsByClub(clubName));
    }

    @GetMapping("/group")
    public TrainingGroupDto getGroupById(long id) {
	TrainingGroup group = trainingGroupService.loadTrainingGroupById(id);
	int numPlayedTr = this.trainingGroupService.calcNumPlayedSessions(group);
	Map<String, Integer> attendance = this.trainingGroupService.calcAttendance(group);
	TrainingGroupDto dto = this.trainingGroupMapper.mapToTrainingGroupDto(group, numPlayedTr, attendance);
	return dto;
    }

    @PostMapping("/updateTrainingGroupDetails")
    public void updateTrainingGroupDetails(@RequestBody TrainingGroupDto groupDto) {
        try {
            TrainingGroup group = null;
            if(groupDto.getId() != -1)
        	group = this.trainingGroupService.loadTrainingGroupById(groupDto.getId());
            else
        	group = new TrainingGroup();
            this.trainingGroupMapper.mapFromTrainingGroupDto(groupDto, group);
            this.trainingGroupService.saveGroup(group);
        }catch (Exception e) {e.printStackTrace();}
    }


}