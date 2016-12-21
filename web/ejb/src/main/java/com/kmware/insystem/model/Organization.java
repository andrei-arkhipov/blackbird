package com.kmware.insystem.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "organizations")
public class Organization extends BasicModel implements java.io.Serializable {
    private static final long serialVersionUID = 1820403377493319532L;

    private String name;
    private String description;;
    private List<Device> devices = new ArrayList<Device>(0);

    public Organization() {
        devices = new ArrayList<Device>();
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name="description", length = 4096)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
	public List<Device> getDevices() {
		return this.devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Organization other = (Organization) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Organization [id=" + id + ", name=" + name + ", description="
				+ description + "]";
	}

	@Override
	public void loadLazyCollections() {
        if (this.devices != null) {
            this.devices.size();
        }
	}

}
