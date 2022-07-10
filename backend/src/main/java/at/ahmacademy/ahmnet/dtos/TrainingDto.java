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
public class TrainingDto {

  private Long id;
  private TrainingGroupSnippetDto group;
  private String clubId;
  private LocalDate date;
  private LocalDate lastDate;
  private String timeslot;
  private int court;
  private LocalTime startTime;
  private int durationMinutes;
  private UserDto trainer;
  private Collection<UserDto> players;
  private Collection<String> attendees;
  private String bulletPoints;
  private String comments;
  private boolean isFree;
  
}

/*

  private long id;
  private LocalDate date;
  private LocalDate lastDate;
  private String timeslot;
  private int weekNum;
  private int durationMinutes;
  private int court;
  private String bulletPoints;
  private String comment;
  private boolean isFree;
  private Collection<String> attendeeIds;
  private long trainingGroupId;
  private String trainerId;
  private String prevTrainer;
  private String clubId;
  
  private String attendees_url;
  private String group_url;
  private String trainer_url;
  private String prevTrainer_url;
  private String club_url;

*/
