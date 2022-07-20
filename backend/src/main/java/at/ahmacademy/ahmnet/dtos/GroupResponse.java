package at.ahmacademy.ahmnet.dtos;

import java.util.Collection;
import java.util.Map;

import at.ahmacademy.ahmnet.model.BallColor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GroupResponse {

  private long id;
  private String trainerId;
  private String clubId;
  private Collection<String> playerIds;
  private Collection<BallColor> ballColors;
  private int numPlayedSessions;
  private Map<String, Integer> attendance;
  
  private String trainer_url;
  private String club_url;
  private String players_url;

}
