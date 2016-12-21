package com.kmware.insystem.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "roles")
public class Role extends BasicModel implements Serializable {
    private static final long serialVersionUID = 4243350487883973237L;

    private String name;
    private String internalName;
    private String description;
    private List<RolePermission> permissions;

    public Role() {
        this.permissions = new ArrayList<RolePermission>(0);
        this.description = null;
    }

    @Column(name="name", nullable = false)
    public String getName() {
        return name;
    }

    @Column(name="internal_name", nullable = false)
    public String getInternalName() {
        return internalName;
    }

    @Column(name="description", length = 4096)
    public String getDescription() {
        return description;
    }

    @XmlTransient
    @OneToMany(mappedBy = "role",cascade={CascadeType.PERSIST,CascadeType.MERGE})
    public List<RolePermission> getPermissions() {
        return permissions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPermissions(List<RolePermission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((internalName == null) ? 0 : internalName.hashCode());
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
        Role other = (Role) obj;
        if (internalName == null) {
            if (other.internalName != null)
                return false;
        } else if (!internalName.equals(other.internalName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Role [name=" + name + ", internalName=" + internalName
               + ", description=" + description + "]";
	}

	@Override
	public void loadLazyCollections() {
        if (this.permissions != null) {
            this.permissions.size();
        }
    }
	
}