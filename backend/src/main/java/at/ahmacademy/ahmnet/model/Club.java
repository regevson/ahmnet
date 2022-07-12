package at.ahmacademy.ahmnet.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Club {

  @Id
  @Column(length = 100)
  private String id;
  
  /*
  @OneToMany(mappedBy="club")
  private Set<User> members;

  @OneToMany(mappedBy="club")
  private Set<TrainingGroup> trainingGroups;
  */

}
