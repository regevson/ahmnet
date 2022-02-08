package at.qe.skeleton.model;

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

@Entity
@Table(name="traininggroup")
public class TrainingGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // otherwise there is problem when saving with empty id
    private long id;

    @ManyToOne
    private User trainer;

    @ManyToOne
    private Club club;

    @ManyToMany
    private Set<User> players;

    @OneToMany(mappedBy = "trainingGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Training> trainings;

    private int numRemainingSessions;

    public long getId() {
	return this.id;
    }

    public User getTrainer() {
	return this.trainer;
    }

    public void setTrainer(User trainer) {
	this.trainer = trainer;
    }

    public Club getClub() {
	return club;
    }

    public void setClub(Club club) {
	this.club = club;
    }

    public Set<User> getPlayers() {
	return this.players;
    }

    public void setPlayers(Set<User> players) {
	this.players = players;
    }

    public int getNumRemainingSessions() {
	return numRemainingSessions;
    }

    public void setNumRemainingSessions(int numRemainingSessions) {
	this.numRemainingSessions = numRemainingSessions;
    }

    public Set<Training> getTrainings() {
	return trainings;
    }

    public void setTrainings(Set<Training> trainings) {
	this.trainings = trainings;
    }

}
