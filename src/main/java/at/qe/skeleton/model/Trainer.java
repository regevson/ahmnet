package at.qe.skeleton.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.springframework.data.domain.Persistable;

@Entity
public class Trainer extends AbstractUser implements Persistable<String>, Serializable, Comparable<Trainer> {

    private static final long serialVersionUID = 1L;





    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Trainer))
            return false;

        Trainer other = (Trainer) obj;
        if (!Objects.equals(this.getUsername(), other.getUsername()))
            return false;

        return true;
    }

    @Override
    public int compareTo(Trainer t) {
        return this.getUsername().compareTo(t.getUsername());
    }

    @Override
    public boolean isNew() {
        return (null == this.getCreateDate());
    }

}
