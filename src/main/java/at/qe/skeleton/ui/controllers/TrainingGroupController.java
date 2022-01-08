package at.qe.skeleton.ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.qe.skeleton.services.TrainingService;

@RequestMapping("/api")
@RestController
@Scope("application")
public class TrainingGroupController {

    @Autowired
    TrainingService trainingGroupService;


}