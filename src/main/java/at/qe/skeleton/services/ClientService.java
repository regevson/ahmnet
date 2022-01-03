package at.qe.skeleton.services;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import at.qe.skeleton.model.Client;
import at.qe.skeleton.repositories.ClientRepository;

@Service
@Scope("application")
public class ClientService extends AbstractUserService<Client, ClientRepository> {

    

}