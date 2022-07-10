package at.ahmacademy.ahmnet.dtos;

import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserSnippetDto {

  private String id;
  private String fullName;
  private Collection<TrainingGroupSnippetDto> groups; //TODO: split this into necessary fields
  private ClubDto club;

}
