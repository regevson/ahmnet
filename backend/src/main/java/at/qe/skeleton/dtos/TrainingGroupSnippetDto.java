package at.qe.skeleton.dtos;

import java.util.Collection;

import at.qe.skeleton.model.Club;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TrainingGroupSnippetDto {

    private long id;
    private UserDto trainer;
    private Club club;
    private Collection<UserDto> players;

}
