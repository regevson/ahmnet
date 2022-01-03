package at.qe.skeleton.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Training {

    @Id
    private long id;

    @ManyToOne
    private Club club;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    private int durationMinutes;
    private String court;
    private String bulletPoints;
    private String comment;

    @ManyToOne
    private TrainingGroup trainingGroup;


    public Club getClub() {
	return club;
    }

    public void setClub(Club club) {
	this.club = club;
    }

    public LocalDateTime getStartTime() {
	return startTime;

    }

    public void setStartTime(LocalDateTime startTime) {
	this.startTime = startTime;
    }

    public int getDurationMinutes() {
	return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
	this.durationMinutes = durationMinutes;
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

    public long getId() {
	return id;
    }


}
