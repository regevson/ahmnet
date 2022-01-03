package at.qe.skeleton.model;

import java.time.LocalTime;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class TrainingGroup {

    @Id
    private long id;

    @ManyToOne
    private Trainer trainer;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Client> clients;

    @ManyToOne
    private Club club;
    
    private LocalTime startTime;

    private int durationMinutes;

    private int replicateWeeks;

    private String court;

    private String bulletPoints;

    private String comment;
    
    private int numRemainingSessions;



    public long getId() {
	return this.id;
    }

    public Trainer getTrainer() {
	return this.trainer;
    }

    public void setTrainer(Trainer trainer) {
	this.trainer = trainer;
    }

    public Set<Client> getClients() {
	return this.clients;
    }

    public void setClients(Set<Client> clients) {
	this.clients = clients;
    }

    public Club getClub() {
	return club;
    }

    public void setClub(Club club) {
	this.club = club;
    }

    public LocalTime getStartTime() {
	return startTime;
    }

    public void setStartTime(LocalTime startTime) {
	this.startTime = startTime;
    }

    public int getDurationMinutes() {
	return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
	this.durationMinutes = durationMinutes;
    }

    public int getReplicateWeeks() {
	return replicateWeeks;
    }

    public void setReplicateWeeks(int replicateWeeks) {
	this.replicateWeeks = replicateWeeks;
    }

    public String getCourt() {
	return court;
    }

    public void setCourt(String court) {
	this.court = court;
    }

    public String getBulletPoints() {
	return bulletPoints;
    }

    public void setBulletPoints(String bulletPoints) {
	this.bulletPoints = bulletPoints;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public int getNumRemainingSessions() {
	return numRemainingSessions;
    }

    public void setNumRemainingSessions(int numRemainingSessions) {
	this.numRemainingSessions = numRemainingSessions;
    }


}
