package at.qe.skeleton.dtos;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TimetableDto {

    private List<String> datesInWeek;
    private List<List<TrainingSnippetDto>> trainings;

}
