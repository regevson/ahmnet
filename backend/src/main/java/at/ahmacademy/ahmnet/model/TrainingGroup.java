package at.ahmacademy.ahmnet.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="traininggroup")
public class TrainingGroup {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // otherwise there is problem when saving with empty id
    private long id;

    @ManyToOne
    private User trainer;

    @ManyToOne
    private Club club;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> players;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "trainingGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Training> trainings;

    private int numPlayedSessions;

}
