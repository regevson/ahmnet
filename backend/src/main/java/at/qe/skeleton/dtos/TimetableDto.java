package at.qe.skeleton.dtos;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TimetableDto {

    private List<String> datesInWeek;
    private HashMap<DayOfWeek, List<TrainingTimeslotDto>> trainings;

}
