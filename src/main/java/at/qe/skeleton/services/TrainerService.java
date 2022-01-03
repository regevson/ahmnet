package at.qe.skeleton.services;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import at.qe.skeleton.model.Trainer;
import at.qe.skeleton.repositories.TrainerRepository;

@Service
@Scope("application")
public class TrainerService extends AbstractUserService<Trainer, TrainerRepository> {

    

}