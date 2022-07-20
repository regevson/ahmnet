package at.ahmacademy.ahmnet.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
public class User implements Persistable<String>, Serializable, Comparable<User> {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(length = 100, name="username")
  private String username;

  @JsonBackReference
  @ManyToOne(optional = false)
  private User createUser;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate;

  @JsonBackReference
  @ManyToOne(optional = true)
  private User updateUser;

  @Temporal(TemporalType.TIMESTAMP)
  private Date updateDate;

  private String password;

  private String firstName;
  private String lastName;
  private Integer birthYear;
  private String email;
  private String phone;

  boolean enabled;

  @ManyToOne
  private Club club;

  @ManyToMany(mappedBy="players")
  @Column(nullable=true)
  private Set<TrainingGroup> trainingGroups;

  @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "User_UserRole")
  @Enumerated(EnumType.STRING)
  private Set<UserRole> roles;


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 59 * hash + Objects.hashCode(this.username);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if(obj == null)
      return false;
    if(!(obj instanceof User))
      return false;
    final User other = (User) obj;
    if(!Objects.equals(this.username, other.username))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "at.qe.skeleton.model.User[ id=" + username + " ]";
  }

  @Override
  public String getId() {
    return getUsername();
  }

  public void setId(String id) {
    setUsername(id);
  }

  @Override
  public boolean isNew() {
    return (null == createDate);
  }

  @Override
  public int compareTo(User o) {
    return this.username.compareTo(o.getUsername());
  }

}

