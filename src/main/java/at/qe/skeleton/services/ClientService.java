package at.qe.skeleton.services;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import at.qe.skeleton.model.Client;

@Service
@Scope("application")
public class ClientService extends AbstractUserService<Client> {

    

}