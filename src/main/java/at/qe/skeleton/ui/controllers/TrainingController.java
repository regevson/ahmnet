package at.qe.skeleton.ui.controllers;

import java.time.DayOfWeek;
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

    @GetMapping("/training")
    public TrainingDetailsDto getTrainingsById(long id) {
	return mapper.mapToTrainingDetailsDto(this.trainingService.loadTraining(id));
    }

    @GetMapping("/trainingsByWeek")
    public HashMap<DayOfWeek, List<TrainingTimeslotDto>> getTrainingsByWeek(String username, int weekNum) {
        List<Training> trainings = this.trainingService.loadTrainingsByTrainerAndWeek(username, weekNum);
	HashMap<DayOfWeek, List<Training>> trainingsByDay = this.trainingService.groupByDay(trainings);

	HashMap<DayOfWeek,List<TrainingTimeslotDto>> trDtoByDay = new HashMap<>();
	for(Map.Entry<DayOfWeek,List<Training>> e : trainingsByDay.entrySet()) {
            List<TrainingTimeslotDto> trDtoList = new ArrayList<>();
	    List<Training> trList = e.getValue();
            for(Training t : trList) 
        	trDtoList.add(mapper.mapToTrainingTimeslotDto(t));
            trDtoByDay.put(e.getKey(), trDtoList);
	}

        return trDtoByDay;
    }

    @PostMapping("/updateTrainingDetails")
    public void setTrainingsDetails(@RequestBody TrainingDetailsDto trDetailsDto) {
    try {
	Training tr = this.trainingService.loadTraining(trDetailsDto.getId());
	mapper.mapFromTrainingDetailsDto(trDetailsDto, tr);
	this.trainingService.saveTraining(tr);
    }catch (Exception e) {e.printStackTrace();}
    }

}
