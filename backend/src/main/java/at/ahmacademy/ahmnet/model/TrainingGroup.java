package at.ahmacademy.ahmnet.model;

import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="traininggroup")
public class TrainingGroup {

  //@Setter(AccessLevel.NONE)
  @Id
  // otherwise there is problem when saving with empty id
  @GeneratedValue(strategy = GenerationType.IDENTITY) 
  private long id;

  @ManyToOne
  private User trainer;

  @ManyToOne
  private Club club;

  @ManyToMany(fetch = FetchType.EAGER)
  private Set<User> players;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "trainingGroup")
  private Set<Training> trainings;

  @Transient
  private int numPlayedSessions;

  @Transient
  private Map<String, Integer> attendance;

}
