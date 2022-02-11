package at.qe.skeleton.ui.controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.qe.skeleton.dtos.TimetableDto;
import at.qe.skeleton.dtos.TrainingDetailsDto;
import at.qe.skeleton.dtos.TrainingMapper;
import at.qe.skeleton.dtos.TrainingTimeslotDto;
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
    public TrainingDetailsDto getNewTraining() {
	TrainingDetailsDto dto = new TrainingDetailsDto();
	dto.setId(-1L);
	return dto;
    }

    @GetMapping("/training")
    public TrainingDetailsDto getTrainingsById(long id) {
	return mapper.mapToTrainingDetailsDto(this.trainingService.loadTrainingById(id));
    }

    @GetMapping("/deleteTraining")
    public void deleteGroup(long id) {
    try {
	this.trainingService.deleteTraining(id);
    }catch(Exception e) { e.printStackTrace();};
    }

    @GetMapping("/trainingsByWeek")
    public TimetableDto getTrainingsByWeek(String trainer, int weekNum) {
    try {
        List<Training> trainings = this.trainingService.loadTrainingsByTrainerAndWeek(trainer, weekNum);
	HashMap<DayOfWeek, List<Training>> trainingsByDay = this.trainingService.groupByDay(trainings);

	HashMap<DayOfWeek,List<TrainingTimeslotDto>> trDtoByDay = new HashMap<>();
	for(Map.Entry<DayOfWeek,List<Training>> e : trainingsByDay.entrySet()) {
            List<TrainingTimeslotDto> trDtoList = new ArrayList<>();
	    List<Training> trList = e.getValue();
            for(Training t : trList) 
        	trDtoList.add(mapper.mapToTrainingTimeslotDto(t));
            trDtoByDay.put(e.getKey(), trDtoList);
	}

	TimetableDto dto = new TimetableDto();
	dto.setDatesInWeek(trainingService.getDatesInWeek(weekNum));
	dto.setTrainings(trDtoByDay);

        return dto;
    }catch(Exception e) { e.printStackTrace();};
        return null;
    }

    @PostMapping("/updateTrainingDetails")
    public void setTrainingsDetails(@RequestBody TrainingDetailsDto trainingDto) {
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
