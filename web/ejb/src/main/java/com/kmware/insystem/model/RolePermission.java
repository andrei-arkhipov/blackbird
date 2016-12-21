package com.kmware.insystem.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "roles_permissions")
public class RolePermission implements Serializable {
    private static final long serialVersionUID = 4122223493565042300L;

    protected long id;
    private Role role;
    private Permission permission;
    private Boolean canRead;
    private Boolean canChange;

    public RolePermission() {
    	role = new Role();
    	permission = new Permission();
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne(fetch=FetchType.EAGER)
    public Role getRole() {
        return role;
    }

    @ManyToOne(fetch=FetchType.EAGER)
    public Permission getPermission() {
        return permission;
    }

    @Column
    public Boolean getCanRead() {
        return canRead;
    }

    @Column
    public Boolean getCanChange() {
        return canChange;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public void setCanRead(Boolean canRead) {
        this.canRead = canRead;
    }

    public void setCanChange(Boolean canChange) {
        this.canChange = canChange;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((permission == null) ? 0 : permission.hashCode());
        result = prime * result + ((role == null) ? 0 : role.hashCode());
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
        RolePermission other = (RolePermission) obj;
        if (permission == null) {
            if (other.permission != null)
                return false;
        } else if (!permission.equals(other.permission))
            return false;
        if (role == null) {
            if (other.role != null)
                return false;
        } else if (!role.equals(other.role))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RolePermission [role=" + role + ", permission=" + permission
                + ", canRead=" + canRead + ", canChange=" + canChange + "]";
    }

}
