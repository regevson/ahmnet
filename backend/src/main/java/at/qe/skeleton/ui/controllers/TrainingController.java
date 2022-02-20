package at.qe.skeleton.ui.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.qe.skeleton.dtos.TimetableDto;
import at.qe.skeleton.dtos.TrainingDto;
import at.qe.skeleton.dtos.TrainingMapper;
import at.qe.skeleton.dtos.TrainingSnippetDto;
import at.qe.skeleton.model.Training;
import at.qe.skeleton.services.TrainingService;
import at.qe.skeleton.services.UserService;

@RequestMapping("/api")
@RestController
@Scope("application")
public class TrainingController {

    @Autowired
    TrainingService trainingService;
    @Autowired
    UserService userService;
    @Autowired
    TrainingMapper mapper;

    @GetMapping("/newTraining")
    public ResponseEntity<?> getNewTraining() {
	TrainingDto dto = new TrainingDto();
	dto.setId(-1L);
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(dto);
    }

    @GetMapping("/training")
    public ResponseEntity<?> getTrainingsById(Long id) {
	TrainingDto dto = mapper.mapToTrainingDto(this.trainingService.loadTrainingById(id));
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(dto);
    }

    @PostMapping("/deleteTraining")
    public ResponseEntity<?> deleteTraining(Long id) {
	this.trainingService.deleteTraining(this.trainingService.loadTrainingById(id));
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .build();
    }

    @PostMapping("/deleteTrainings")
    public ResponseEntity<?> deleteTrainings(Long[] trainingIds) {
	this.trainingService.deleteTrainings(trainingIds);
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .build();
    }

    @PostMapping("/freeTraining")
    public ResponseEntity<?> freeTraining(Long id) {
	this.trainingService.freeTraining(trainingService.loadTrainingById(id));
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .build();
    }

    @PostMapping("/freeTrainings")
    public ResponseEntity<?> freeTrainings(Long[] trainingIds) {
	this.trainingService.freeTrainings(trainingIds);
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .build();
    }

    @PostMapping("/grabTraining")
    public ResponseEntity<?> grabTraining(Long id) {
	this.trainingService.grabTraining(trainingService.loadTrainingById(id));
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .build();
    }

    @PostMapping("/grabTrainings")
    public ResponseEntity<?> grabTrainings(Long[] trainingIds) {
	this.trainingService.grabTrainings(trainingIds);
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .build();
    }

    @GetMapping("/trainingsByWeek")
    public ResponseEntity<?> getTrainingsByWeek(String trainer, Integer weekNum) {
        List<List<Training>> trainingsByDay = this.trainingService.getTrainingsByWeek(trainer, weekNum);
        List<List<TrainingSnippetDto>> dtoList = new ArrayList<>();

        for(List<Training> dayList : trainingsByDay)
            dtoList.add(mapper.mapToTrainingSnippetDto(dayList));

        TimetableDto dto = this.mapper.mapToTimetableDto(trainingService.getDatesInWeek(weekNum), dtoList);

        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(dto);
    }
    
    @GetMapping("/availableTrainings")
    public ResponseEntity<?> getFreeTrainings(Integer weekNum) {
	List<List<Training>> trainingsByDay = this.trainingService.loadFreeTrainingsByWeek(weekNum);
	List<List<TrainingSnippetDto>> dtoList = new ArrayList<>();

	for(List<Training> dayList : trainingsByDay)
            dtoList.add(mapper.mapToTrainingSnippetDto(dayList));

	TimetableDto dto = this.mapper.mapToTimetableDto(trainingService.getDatesInWeek(weekNum), dtoList);
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(dto);
    }
    

    @PostMapping("/updateTrainingDetails")
    public ResponseEntity<?> setTrainingsDetails(@RequestBody TrainingDto trainingDto) {
        Training training = null;
        if(trainingDto.getId() == -1)
            training = new Training();
        else
            training = this.trainingService.loadTrainingById(trainingDto.getId());
	mapper.mapFromTrainingDetailsDto(trainingDto, training);
	this.trainingService.saveRecurringTrainings(training);
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
    }

}
