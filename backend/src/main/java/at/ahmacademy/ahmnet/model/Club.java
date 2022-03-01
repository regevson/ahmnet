package at.ahmacademy.ahmnet.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Club {

    @Id
    @Column(length = 100, name="name")
    private String name;

}
