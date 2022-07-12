package at.ahmacademy.ahmnet.dtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

import at.ahmacademy.ahmnet.model.Club;
import at.ahmacademy.ahmnet.model.TrainingGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TrainingResponse {

  private long id;
  private LocalDate date;
  private LocalDate lastDate;
  private LocalTime startTime;
  private String timeslot;
  private int weekNum;
  private int durationMinutes;
  private int court;
  private String bulletPoints;
  private String comments;
  private boolean isFree;
  private Collection<String> playerIds;
  private Collection<String> attendeeIds;
  private long groupId;
  private String trainerId;
  private String prevTrainerId;
  private String clubId;
  
  private String players_url;
  private String attendees_url;
  private String group_url;
  private String trainer_url;
  private String prevTrainer_url;
  private String club_url;
  
}