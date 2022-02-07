package at.qe.skeleton.dtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

import at.qe.skeleton.model.Club;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TrainingDetailsDto {

    private Long id;
    private Club club;
    private LocalDate date;
    private String timeslot;
    private LocalTime startTime;
    private int durationMinutes;
    private String trainerFn;
    private String trainerLn;
    private Collection<UserDto> players;
    private Collection<String> attendees;
    private String bulletPoints;
    private String comments;

}
