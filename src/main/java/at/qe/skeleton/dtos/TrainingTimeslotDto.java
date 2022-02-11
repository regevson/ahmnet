package at.qe.skeleton.dtos;

import java.time.LocalDate;

import at.qe.skeleton.model.Club;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TrainingTimeslotDto {

    private long id;
    private Club club;
    private String date;
    private String timeslot;
    private String court;

}
