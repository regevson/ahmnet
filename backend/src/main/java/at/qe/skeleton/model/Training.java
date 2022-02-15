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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Training {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // otherwise there is problem when saving with empty id
    private long id;

    @Setter(AccessLevel.NONE)
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateTime;

    private int weekNum;
    private int durationMinutes;
    private int court;
    private String bulletPoints;
    private String comment;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean isFree;

    @ManyToMany
    private Set<User> attendees;

    @ManyToOne
    @JoinColumn(name="trainingGroupId")
    private TrainingGroup trainingGroup;
    
    @Setter(AccessLevel.NONE)
    @ManyToOne
    private User trainer;

    @Setter(AccessLevel.NONE)
    @ManyToOne
    private User originalTrainer;

    @ManyToOne
    private Club club;

    public void setDateTime(LocalDateTime dateTime) {
	this.dateTime = dateTime;
	this.extractWeekNum(dateTime);
    }

    private void extractWeekNum(LocalDateTime dateTime) {
	LocalDate date = dateTime.toLocalDate();
        this.setWeekNum(date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
    }

    public void setTrainer(User trainer) {
	this.trainer = trainer;
	if(this.getOriginalTrainer() == null)
	    this.originalTrainer = trainer;
    }

}
