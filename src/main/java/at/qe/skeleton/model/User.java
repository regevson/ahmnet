package at.qe.skeleton.model;

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
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.domain.Persistable;

/**
 * Entity representing users.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Entity
public class User implements Persistable<String>, Serializable, Comparable<User> {

    private static final long serialVersionUID = 1L;

    @Id // is primary_key
    @Column(length = 100) // must have specific length (upper boundary)
    private String username;

		// n:1 ->	I have been created by exactly 1 user, this user can create n users
    @ManyToOne(optional = false) // wenn createUser==null -> user KANN NICHT in db gestored werden
    private User createUser; // the user that created me

    @Column(nullable = false) // this field cannot be null (if it wants to be stored in db)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

		// n:1 ->	my last update is made by exactly 1 user, this user can update n users
    @ManyToOne(optional = true) // wenn createUser==null -> user KANN trotzdem in db gestored werden
    private User updateUser; // the user that updated me -> must not be one

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    private String password;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    boolean enabled;

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "User_UserRole")
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
	}

    public void setPassword(String password) {
        this.password = password;
	}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(User updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.username);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
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
