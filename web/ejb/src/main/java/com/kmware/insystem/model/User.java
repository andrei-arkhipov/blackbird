package com.kmware.insystem.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="users")
public class User extends BasicModel implements Serializable {
    private static final long serialVersionUID = 4684865219930979685L;

    private String userName;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private Boolean isActive;
    private List<Role> roles;

    public User() {
        this.roles = new ArrayList<Role>();
        this.isActive = true;
    }

    @Column(name="user_name", nullable = false)
    public String getUserName() {
        return userName;
    }

    @XmlTransient
    @Column(name="password", nullable = false)
    public String getPassword() {
        return password;
    }

    @Column(name="first_name", nullable = false)
    public String getFirstName() {
        return firstName;
    }

    @Column(name="middle_name")
    public String getMiddleName() {
        return middleName;
    }

    @Column(name="last_name", nullable = false)
    public String getLastName() {
        return lastName;
    }

    @Column(name="email")
    public String getEmail() {
        return email;
    }

    @Column(name="is_active")
    public Boolean getIsActive() {
        return isActive;
    }

    @XmlTransient
    @ManyToMany
    @JoinTable(name = "users_roles",  
    joinColumns = @JoinColumn(name = "users_id",referencedColumnName="id"), 
    inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName="id"))
    public List<Role> getRoles() {
        return roles;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (userName == null) {
            if (other.userName != null)
                return false;
        } else if (!userName.equals(other.userName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "User [userName=" + userName + ", password=" + password
                + ", firstName=" + firstName + ", middleName=" + middleName
                + ", lastName=" + lastName + ", email=" + email + ", isActive="
                + isActive + "]";
    }

    @Override
    public void loadLazyCollections() {
        if (this.roles != null) {
            this.roles.size();
            for (Role r : this.getRoles()) {
            	r.getPermissions().size();
            }
        }
    }
	
}
