package at.qe.skeleton.dtos;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TrainingTimeslotDto {

    private long id;
    private String clubName;
    private LocalDate date;
    private String timeslot;
    private String court;

}
