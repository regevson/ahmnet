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
    private Club club;
    private String date;
    private String timeslot;
    private LocalTime startTime;
    private int durationMinutes;
    private int court;
    private boolean isFree;
    private String trainerId;
    private String prevTrainerId;

}
