package at.qe.skeleton.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import org.springframework.data.domain.Persistable;

@Entity
public class Client extends AbstractUser implements Persistable<String>, Serializable, Comparable<Client> {

    private static final long serialVersionUID = 1L;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<TrainingGroup> groups;














    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Client))
            return false;

        Client other = (Client) obj;
        if (!Objects.equals(this.getUsername(), other.getUsername()))
            return false;

        return true;
    }

    @Override
    public int compareTo(Client t) {
        return this.getUsername().compareTo(t.getUsername());
    }

    @Override
    public boolean isNew() {
        return (null == this.getCreateDate());
    }

}