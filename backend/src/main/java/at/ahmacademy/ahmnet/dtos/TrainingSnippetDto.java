package at.ahmacademy.ahmnet.dtos;

import java.time.LocalTime;

import at.ahmacademy.ahmnet.model.Club;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TrainingSnippetDto {

  private long id;
  private long groupId;
  private String clubId;
  private String groupClubId;
  private String date;
  private String timeslot;
  private LocalTime startTime;
  private int court;
  private boolean isFree;
  private String trainerId;
  private String prevTrainerId;

}
