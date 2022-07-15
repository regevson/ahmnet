package at.ahmacademy.ahmnet.controllers;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import at.ahmacademy.ahmnet.dtos.TrainingMapper;
import at.ahmacademy.ahmnet.dtos.TrainingRequest;
import at.ahmacademy.ahmnet.dtos.TrainingResponse;
import at.ahmacademy.ahmnet.model.Training;
import at.ahmacademy.ahmnet.services.training.TrainingPathValidationService;
import at.ahmacademy.ahmnet.services.training.TrainingService;
import at.ahmacademy.ahmnet.services.user.UserService;

@RequestMapping("/api")
@RestController
@Scope("application")
public class TrainingController {

  @Autowired
  TrainingService trService;
  @Autowired
  UserService userService;
  @Autowired
  TrainingMapper mapper;
  @Autowired
  TrainingPathValidationService pathValidator;


  @GetMapping("/trainings/{trainingIds}")
  public ResponseEntity<?> getTrainingsById(@PathVariable Long[] trainingIds) {
    Collection<Training> trainings = Arrays.stream(trainingIds).map(trService::loadTrainingById).collect(Collectors.toSet());
    List<TrainingResponse> dtos = mapper.mapToDto(trainings);
    if(dtos.size() == 1) return ResponseEntity.status(HttpStatus.OK).body(dtos.get(0));
    return ResponseEntity.status(HttpStatus.OK).body(dtos);
  }

  @DeleteMapping("/trainings/{trainingIds}")
  public ResponseEntity<?> deleteTrainingsById(@PathVariable Long[] trainingIds) {
    Set<Training> trainings = Arrays.stream(trainingIds).map(trService::loadTrainingById).collect(Collectors.toSet());
    trainings.stream().forEach(trService::deleteTraining);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping(value = {"/trainings/{trainingIds}/actions/free",
                        "/trainings/{trainingIds}/actions/free/{notify}"})
  public ResponseEntity<?> freeTrainingsById(@PathVariable Long[] trainingIds,
                                             @PathVariable Optional<String> notify) {
    List<Training> trainings = Arrays.stream(trainingIds).map(trService::loadTrainingById).collect(Collectors.toList());
    trService.freeTrainings(trainings, notify.orElse("").equals("notify"));
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping(value = {"/trainings/{trainingIds}/actions/grab",
                        "/trainings/{trainingIds}/actions/grab/{notify}"})
  public ResponseEntity<?> grabTrainingsById(@PathVariable Long[] trainingIds,
                                             @PathVariable Optional<String> notify) {
    List<Training> trainings = Arrays.stream(trainingIds).map(trService::loadTrainingById).collect(Collectors.toList());
    trService.grabTrainings(trainings, notify.orElse("").equals("notify"));
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping(value = "/trainings", params = {"weekNum"})
  public ResponseEntity<?> getAllFreeTrainingsByWeekNum(Integer weekNum, Optional<String> exclTrainerId) {
    List<List<Training>> trainingsByDay = trService.loadFreeTrainings(weekNum, exclTrainerId);
    List<List<TrainingResponse>> dtoList = mapper.mapToDto(trainingsByDay);
    TimetableDto dto = mapper.mapToTimetableDto(trService.getDatesInWeek(weekNum), dtoList);
    return ResponseEntity.status(HttpStatus.OK).body(dto);
  }

  @GetMapping(value = "/trainers/{id}/trainings", params = {"weekNum"})
  public ResponseEntity<?> getTrainingsByTrainerAndWeekNum(@PathVariable String id, Integer weekNum, 
                                                           Optional<Boolean> free) {
    List<List<Training>> trainingsByDay = trService.loadTrainingsByTrainer(id, weekNum, free);
    List<List<TrainingResponse>> dtoList = mapper.mapToDto(trainingsByDay);
    TimetableDto dto = mapper.mapToTimetableDto(trService.getDatesInWeek(weekNum), dtoList);
    return ResponseEntity.status(HttpStatus.OK).body(dto);
  }

  @PostMapping("/trainings")
  public ResponseEntity<?> createNewTraining(@RequestBody TrainingRequest trainingDto) {
    Training training = mapper.mapToEntity(null, trainingDto);
    trService.saveNewTraining(training);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PutMapping("/trainings/{trainingId}")
  public ResponseEntity<?> updateExistingTraining(@PathVariable Long trainingId,
                                                  @RequestBody TrainingRequest trainingDto) {
    Training training = mapper.mapToEntity(trainingId, trainingDto);
    trService.updateTraining(training);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
  
}
