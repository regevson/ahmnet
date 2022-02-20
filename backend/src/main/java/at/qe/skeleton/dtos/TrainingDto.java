package at.qe.skeleton.dtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

import at.qe.skeleton.model.Club;
import at.qe.skeleton.model.TrainingGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TrainingDto {

    private Long id;
    private TrainingGroupSnippetDto group;
    private Club club;
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
