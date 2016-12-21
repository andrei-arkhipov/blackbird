package com.kmware.insystem.rest.helper;

import com.kmware.insystem.model.Direction;
import com.kmware.insystem.rest.helper.Organization;;

public class Device {
	protected long id;
    private String deviceNumber;
    private String description;
    private Organization organization;
    private Direction direction;

	public Device(com.kmware.insystem.model.Device holder) {
        this.id = holder.getId();
        this.deviceNumber = holder.getDeviceNumber();
        this.description = holder.getDescription();
        this.direction = holder.getDirection();
        this.organization = new Organization(holder.getOrganization());
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDeviceNumber() {
		return deviceNumber;
	}
	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Organization getOrganization() {
		return organization;
	}
//	public void setOrganization(Organization organization) {
//		this.organization = organization;
//	}
	public Direction getDirection() {
		return direction;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

}
