package at.ahmacademy.ahmnet.model;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Training {

  @Setter(AccessLevel.NONE)
  @Id
  // otherwise there is problem when saving with empty id
  @GeneratedValue(strategy = GenerationType.IDENTITY) 
  private long id;

  @Setter(AccessLevel.NONE)
  @Column(columnDefinition = "TIMESTAMP")
  private LocalDateTime dateTime;

  @Column(columnDefinition = "TIMESTAMP")
  private LocalDate lastDate;

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
  private User prevTrainer;

  @ManyToOne
  private Club club;
  
  public Training(Training toClone) {
    this.setTrainingGroup(toClone.getTrainingGroup());
    this.setTrainer(toClone.getTrainer());
    this.setPrevTrainer(toClone.getPrevTrainer());
    this.setClub(toClone.getClub());
    this.setCourt(toClone.getCourt());
    this.setDateTime(toClone.getDateTime());
    this.setLastDate(toClone.getLastDate());
    this.setDurationMinutes(toClone.getDurationMinutes());
    this.setAttendees(toClone.getAttendees());
    this.setBulletPoints(toClone.getBulletPoints());
    this.setComment(toClone.getComment());
    this.setIsFree(toClone.getIsFree());
  }

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
    if(this.prevTrainer == null)
      this.setPrevTrainer(trainer);
  }
  
  public void setPrevTrainer(User prevTrainer) {
    this.prevTrainer = prevTrainer;
  }

}
