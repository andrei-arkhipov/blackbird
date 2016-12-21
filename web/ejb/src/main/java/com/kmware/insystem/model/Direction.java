package com.kmware.insystem.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "directions")
public class Direction extends BasicModel implements Serializable {
    private static final long serialVersionUID = -5749758168143756649L;

    private String name;
    private String internalName;
    private String description;

    public Direction() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public void setDescription(String description) {
        this.description = description;
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
        Direction other = (Direction) obj;
        if (internalName == null) {
            if (other.internalName != null)
                return false;
        } else if (!internalName.equals(other.internalName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Direction [name=" + name + ", internalName=" + internalName
                + ", description=" + description + "]";
    }

	@Override
	public void loadLazyCollections() {
		// TODO Auto-generated method stub
		
	}

}
