package at.qe.skeleton.ui.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
    public TrainingDto getNewTraining() {
	TrainingDto dto = new TrainingDto();
	dto.setId(-1L);
	return dto;
    }

    @GetMapping("/training")
    public TrainingDto getTrainingsById(long id) {
	return mapper.mapToTrainingDto(this.trainingService.loadTrainingById(id));
    }

    @GetMapping("/deleteTraining")
    public void deleteTraining(long id) {
    try {
	this.trainingService.deleteTraining(id);
    }catch(Exception e) { e.printStackTrace();};
    }

    @GetMapping("/freeTraining")
    public void freeTraining(long id) {
    try {
	this.trainingService.freeTraining(id);
    }catch(Exception e) { e.printStackTrace();};
    }

    @GetMapping("/grabTraining")
    public void grabTraining(long id) {
    try {
	this.trainingService.grabTraining(id);
    }catch(Exception e) { e.printStackTrace();};
    }

    @GetMapping("/trainingsByWeek")
    public TimetableDto getTrainingsByWeek(String trainer, int weekNum) {
    try {
	List<List<Training>> trainingsByDay = this.trainingService.getTrainingsByWeek(trainer, weekNum);
	List<List<TrainingSnippetDto>> dtoList = new ArrayList<>();

	for(List<Training> dayList : trainingsByDay)
            dtoList.add(mapper.mapToTrainingSnippetDto(dayList));

	return this.mapper.mapToTimetableDto(trainingService.getDatesInWeek(weekNum), dtoList);
    }catch(Exception e) { e.printStackTrace();};
        return null;
    }
    
    @GetMapping("/availableTrainings")
    public TimetableDto getFreeTrainings(int weekNum) {
    try {
	List<List<Training>> trainingsByDay = this.trainingService.loadFreeTrainingsByWeek(weekNum);
	List<List<TrainingSnippetDto>> dtoList = new ArrayList<>();

	for(List<Training> dayList : trainingsByDay)
            dtoList.add(mapper.mapToTrainingSnippetDto(dayList));

	return this.mapper.mapToTimetableDto(trainingService.getDatesInWeek(weekNum), dtoList);
    }catch(Exception e) { e.printStackTrace();};
        return null;
    }
    

    @PostMapping("/updateTrainingDetails")
    public void setTrainingsDetails(@RequestBody TrainingDto trainingDto) {
    try {
        Training training = null;
        if(trainingDto.getId() != -1)
            training = this.trainingService.loadTrainingById(trainingDto.getId());
        else
            training = new Training();
	mapper.mapFromTrainingDetailsDto(trainingDto, training);
	this.trainingService.saveTraining(training);
    }catch (Exception e) {e.printStackTrace();}
    }

}
