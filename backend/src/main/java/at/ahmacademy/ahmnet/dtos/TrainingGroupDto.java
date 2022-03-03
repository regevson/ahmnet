package at.ahmacademy.ahmnet.dtos;

import java.util.Collection;
import java.util.Map;

import at.ahmacademy.ahmnet.model.Club;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TrainingGroupDto {

  private long id;
  private UserDto trainer;
  private Club club;
  private Collection<UserDto> players;
  private int numPlayedSessions;
  private Map<String, Integer> attendance;

}
