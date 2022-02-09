package at.qe.skeleton.ui.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.qe.skeleton.dtos.TrainingGroupDto;
import at.qe.skeleton.dtos.TrainingGroupMapper;
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

    @GetMapping("/allClubs")
    public Collection<Club> getAllClubs() {
	return this.trainingGroupService.loadAllClubs();
    }

    @GetMapping("/allGroups")
    public Collection<TrainingGroupDto> getAllGroups() {
	return this.trainingGroupMapper.mapToTrainingGroupDto(trainingGroupService.loadAllGroups());
    }
    
    @GetMapping("/deleteGroup")
    public void deleteGroup(long id) {
    try {
	this.trainingGroupService.deleteGroup(id);
    }catch(Exception e) { e.printStackTrace();};
    }

    @GetMapping("/groupsByClub")
    public Collection<TrainingGroupDto> getClubsByClub(String clubName) {
        return this.trainingGroupMapper.mapToTrainingGroupDto(trainingGroupService.loadTrainingGroupsByClub(clubName));
    }

    @GetMapping("/group")
    public TrainingGroupDto getGroupById(long id) {
	return this.trainingGroupMapper.mapToTrainingGroupDto(trainingGroupService.loadTrainingGroupById(id));
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