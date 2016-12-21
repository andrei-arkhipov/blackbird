package com.kmware.insystem.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "devices")
public class Device extends BasicModel implements Serializable {
    private static final long serialVersionUID = -434829281138931401L;

    private String deviceNumber;
    private String description;
    private Organization organization = new Organization();
    private Direction direction = new Direction();

    public Device() {
        this.organization = new Organization();
        this.direction = new Direction();
        this.description = null;
    }

    @Column(name = "device_number", nullable = false, length = 16)
    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    @Column(name="description", length = 4096)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "direction_id", nullable = false)
    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id", nullable = false)
    public Organization getOrganization() {
        return this.organization;
    }

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((deviceNumber == null) ? 0 : deviceNumber.hashCode());
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
        Device other = (Device) obj;
        if (deviceNumber == null) {
            if (other.deviceNumber != null)
                return false;
        } else if (!deviceNumber.equals(other.deviceNumber))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Device [deviceNumber=" + deviceNumber + ", description=" + description + "]";
	}

	@Override
	public void loadLazyCollections() {
		// TODO Auto-generated method stub
		
	}
	
}