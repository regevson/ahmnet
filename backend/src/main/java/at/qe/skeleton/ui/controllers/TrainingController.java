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
    public ResponseEntity<?> getTrainingsById(long id) {
	TrainingDto dto = mapper.mapToTrainingDto(this.trainingService.loadTrainingById(id));
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(dto);
    }

    @GetMapping("/deleteTraining")
    public ResponseEntity<?> deleteTraining(long id) {
	this.trainingService.deleteTraining(this.trainingService.loadTrainingById(id));
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .build();
    }

    @GetMapping("/freeTraining")
    public ResponseEntity<?> freeTraining(long id) {
	this.trainingService.freeTraining(trainingService.loadTrainingById(id));
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .build();
    }

    @GetMapping("/grabTraining")
    public ResponseEntity<?> grabTraining(long id) {
	this.trainingService.grabTraining(trainingService.loadTrainingById(id));
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .build();
    }

    @GetMapping("/trainingsByWeek")
    public ResponseEntity<?> getTrainingsByWeek(String trainer, int weekNum) {
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
    public ResponseEntity<?> getFreeTrainings(int weekNum) {
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
        if(trainingDto.getId() != -1)
            training = this.trainingService.loadTrainingById(trainingDto.getId());
        else
            training = new Training();
	mapper.mapFromTrainingDetailsDto(trainingDto, training);
	this.trainingService.saveTraining(training);
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
    }

}
