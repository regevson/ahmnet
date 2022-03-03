package at.ahmacademy.ahmnet.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.ahmacademy.ahmnet.dtos.TimetableDto;
import at.ahmacademy.ahmnet.dtos.TrainingDto;
import at.ahmacademy.ahmnet.dtos.TrainingMapper;
import at.ahmacademy.ahmnet.dtos.TrainingSnippetDto;
import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.services.TrainingService;
import at.ahmacademy.ahmnet.services.UserService;

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


    @GetMapping("/trainers/{trainerId}/trainings/{trainingId}")
    public ResponseEntity<?> getTrainingById(@PathVariable String trainerId, @PathVariable Long trainingId) {
	TrainingDto dto = mapper.mapToTrainingDto(trainingService.loadTrainingById(trainerId, trainingId));
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(dto);
    }

    @DeleteMapping("/trainers/{trainerId}/trainings/{trainingIds}")
    public ResponseEntity<?> deleteTrainings(@PathVariable String trainerId, 
	    				    @PathVariable Long[] trainingIds) {
	trainingService.deleteTrainings(trainerId, trainingIds);
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .build();
    }

    @PostMapping(value={"/trainers/{trainerId}/trainings/{trainingIds}/actions/free", 
    			"/trainers/{trainerId}/trainings/{trainingIds}/actions/free/{notify}"})
    public ResponseEntity<?> freeTrainings(@PathVariable String trainerId, 
	    				   @PathVariable Long[] trainingIds, 
	    				   @PathVariable Optional<String> notify) {
	trainingService.freeTrainings(trainerId, trainingIds, notify.orElse("").equals("notify"));
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .build();
    }

    @PostMapping(value={"/batch/trainings/{ids}/actions/grab", "/batch/trainings/{ids}/actions/grab/{notify}"})
    public ResponseEntity<?> grabTrainings(@PathVariable Long[] ids, @PathVariable Optional<String> notify) {
	trainingService.grabTrainings(ids, notify.orElse("").equals("notify"));
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .build();
    }

    @GetMapping(value="/batch/trainings", params={"weekNum"})
    public ResponseEntity<?> getTrainingsBatch(Integer weekNum, Optional<String> exclTrainerId) {
        List<List<Training>> trainingsByDay = trainingService.loadFreeTrainings(weekNum, exclTrainerId);
        List<List<TrainingSnippetDto>> dtoList = mapper.mapToTrainingSnippetDto(trainingsByDay);
        TimetableDto dto = mapper.mapToTimetableDto(trainingService.getDatesInWeek(weekNum), dtoList);
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(dto);
    }

    @GetMapping(value="/trainers/{id}/trainings", params={"weekNum"})
    public ResponseEntity<?> getTrainings(@PathVariable String id, Integer weekNum, Optional<Boolean> free) {
        List<List<Training>> trainingsByDay = trainingService.loadTrainingsByTrainer(id, weekNum, free);
        List<List<TrainingSnippetDto>> dtoList = mapper.mapToTrainingSnippetDto(trainingsByDay);
        TimetableDto dto = mapper.mapToTimetableDto(trainingService.getDatesInWeek(weekNum), dtoList);
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(dto);
    }
    
    @PostMapping("trainers/{id}/trainings")
    public ResponseEntity<?> createNewTraining(@PathVariable String id, @RequestBody TrainingDto trainingDto) {
        Training training = new Training();
	mapper.mapFromTrainingDto(trainingDto, training);
	trainingService.saveNewTraining(id, training);
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
    }

    @PutMapping("trainers/{trainerId}/trainings/{trainingId}")
    public ResponseEntity<?> setTrainingsDetails(@PathVariable String trainerId,
	    					 @PathVariable Long trainingId,
	    					 @RequestBody TrainingDto trainingDto) {
        Training training = trainingService.loadTrainingById(trainerId, trainingId);
	mapper.mapFromTrainingDto(trainingDto, training);
	trainingService.updateTraining(trainerId, trainingId, training);
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
    }

}
