package at.qe.skeleton.ui.controllers;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/trainingsByWeek")
    public HashMap<DayOfWeek, List<Training>> getTrainingsByWeek(String username, int weekNum) {
        List<Training> trainings = this.trainingService.loadTrainingsByTrainerAndWeek(username, weekNum);
	HashMap<DayOfWeek, List<Training>> trainingsByDay = this.trainingService.groupByDay(trainings);
        return trainingsByDay;
    }

/*
    @PostMapping("/updateTrainingDetails")
    public void setTrainingsDetails(long id, String[] attendees, String bulletPoints, String comments) {
	try {
	Set<User> users = this.userService.loadUsersById(attendees);
        this.trainingService.updateTrainingDetails(id, users, bulletPoints, comments);
	}catch(Exception e) {e.printStackTrace();}
    }
    */

}
