package at.qe.skeleton.ui.controllers;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getNewGroup() {
	TrainingGroupDto dto = new TrainingGroupDto();
	dto.setId(-1L);
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(dto);
    }

    @GetMapping("/allClubs")
    public ResponseEntity<?> getAllClubs() {
	Collection<Club> dtos = this.trainingGroupService.loadAllClubs();
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(dtos);
    }

    @GetMapping("/allGroups")
    public ResponseEntity<?> getAllGroups() {
	Collection<TrainingGroupSnippetDto> dtos = this.trainingGroupMapper.mapToTrainingGroupSnippetDto(trainingGroupService.loadAllGroups());
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(dtos);
    }
    
    @GetMapping("/deleteGroup")
    public ResponseEntity<?> deleteGroup(long id) {
	this.trainingGroupService.deleteGroup(this.trainingGroupService.loadTrainingGroupById(id));
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
    }

    @GetMapping("/groupsByClub")
    public ResponseEntity<?> getGroupsByClub(String clubName) {
        Collection<TrainingGroupSnippetDto> dtos = this.trainingGroupMapper.mapToTrainingGroupSnippetDto(trainingGroupService.loadTrainingGroupsByClub(clubName));
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(dtos);
    }

    @GetMapping("/group")
    public ResponseEntity<?> getGroupById(long id) {
	TrainingGroup group = trainingGroupService.loadTrainingGroupById(id);
	int numPlayedTr = this.trainingGroupService.calcNumPlayedSessions(group);
	Map<String, Integer> attendance = this.trainingGroupService.calcAttendance(group);
	TrainingGroupDto dto = this.trainingGroupMapper.mapToTrainingGroupDto(group, numPlayedTr, attendance);
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(dto);
    }

    @PostMapping("/updateTrainingGroupDetails")
    public ResponseEntity<?> updateTrainingGroupDetails(@RequestBody TrainingGroupDto groupDto) {
        TrainingGroup group = null;
        if(groupDto.getId() != -1)
            group = this.trainingGroupService.loadTrainingGroupById(groupDto.getId());
        else
            group = new TrainingGroup();
        this.trainingGroupMapper.mapFromTrainingGroupDto(groupDto, group);
        this.trainingGroupService.saveGroup(group);
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
    }


}