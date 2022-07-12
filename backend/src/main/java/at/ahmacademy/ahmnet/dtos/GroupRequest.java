package at.ahmacademy.ahmnet.dtos;

import java.util.Collection;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GroupRequest {

  private String trainerId;
  private String clubId;
  private Collection<String> playerIds;
  private Collection<Long> trainingIds;

}
