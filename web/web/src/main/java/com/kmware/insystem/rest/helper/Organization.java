package com.kmware.insystem.rest.helper;

public class Organization {
	private long id;
    private String name;
    private String description;
    
    public Organization(com.kmware.insystem.model.Organization holder) {
    	this.id = holder.getId();
    	this.name = holder.getName();
    	this.description = holder.getDescription();
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


}
