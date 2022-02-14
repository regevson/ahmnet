package at.qe.skeleton.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ColumnDefault;


@Entity
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // otherwise there is problem when saving with empty id
    private long id;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateTime;

    private int weekNum;
    private int durationMinutes;
    private String court;
    private String bulletPoints;
    private String comment;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean isFree;

    @ManyToMany
    private Set<User> attendees;

    @ManyToOne
    @JoinColumn(name="trainingGroupId")
    private TrainingGroup trainingGroup;
    
    @ManyToOne
    private User trainer;

    @ManyToOne
    private User originalTrainer;

    @ManyToOne
    private Club club;

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

    public User getTrainer() {
	return trainer;
    }

    public void setTrainer(User trainer) {
	this.trainer = trainer;
	if(this.getOriginalTrainer() == null)
	    this.originalTrainer = trainer;
    }

    public Club getClub() {
	return club;
    }

    public void setClub(Club club) {
	this.club = club;
    }

    public boolean isFree() {
	return isFree;
    }

    public void setFree(boolean isFree) {
	this.isFree = isFree;
    }

    public User getOriginalTrainer() {
	return originalTrainer;
    }

}
