package at.ahmacademy.ahmnet.model;

import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="traininggroup")
public class TrainingGroup {

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
  
  @ElementCollection(targetClass = BallColor.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "ball_color")
  @Enumerated(EnumType.STRING)
  private Set<BallColor> ballColors;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "trainingGroup", 
      cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Training> trainings;

  @Transient
  private int numPlayedSessions;

  @Transient
  private Map<String, Integer> attendance;

}
