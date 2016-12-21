package com.kmware.insystem.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "permissions")
public class Permission implements Serializable {
    private static final long serialVersionUID = 6127948562757170687L;

    protected long id;
    private String name;
    private String internalName;
    private String description;
    private Integer orderNumber;
    private List<RolePermission> permissions;

    public Permission() {
        this.permissions = new ArrayList<RolePermission>();
        this.description = null;
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

    @Column(name="order_number")
    public Integer getOrderNumber() {
		return orderNumber;
	}

    @XmlTransient
    @OneToMany(mappedBy = "permission")
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

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
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
        Permission other = (Permission) obj;
        if (internalName == null) {
            if (other.internalName != null)
                return false;
        } else if (!internalName.equals(other.internalName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Permission [name=" + name + ", internalName=" + internalName
                + ", description=" + description + ", permissions="
                + permissions + "]";
    }

}
