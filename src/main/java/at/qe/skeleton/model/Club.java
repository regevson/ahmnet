package at.qe.skeleton.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Club {

    @Id
    @Column(length = 100, name="name")
    private String name;

    public String getName() {
	return name;
    }

/*
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Trainer> trainer;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Client> clients;
    */


}
