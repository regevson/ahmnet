package at.qe.skeleton.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.IsoFields;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Training {

    @Id
    private long id;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateTime;

    private int weekNum;
    private int durationMinutes;
    private String court;
    private String bulletPoints;
    private String comment;

    @ManyToMany
    private Set<User> attendees;

    @ManyToOne
    private TrainingGroup trainingGroup;



    public LocalDateTime getDateTime() {
	return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
	this.dateTime = dateTime;
	this.setWeekNum(dateTime);
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

    public int getWeekNum() {
	return weekNum;
    }

    public void setWeekNum(int weekNum) {
	this.weekNum = weekNum;
    }

    private void setWeekNum(LocalDateTime dateTime) {
	LocalDate date = dateTime.toLocalDate();
        setWeekNum(date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
    }

    public TrainingGroup getTrainingGroup() {
	return trainingGroup;
    }

    public void setTrainingGroup(TrainingGroup trainingGroup) {
	this.trainingGroup = trainingGroup;
    }

    public Set<User> getAttendees() {
	return attendees;
    }

    public void setAttendees(Set<User> attendance) {
	this.attendees = attendance;
    }

}
