package at.qe.skeleton.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="traininggroup")
public class TrainingGroup {

    @Id
    private long id;

    @ManyToOne
    private User trainer;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> players;

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


}
