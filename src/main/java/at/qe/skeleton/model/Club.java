package at.qe.skeleton.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Club {

    @Id
    @Column(length = 100)
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Trainer> trainer;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Client> clients;

}
