package com.kmware.insystem.rest.helper;

public class CardHolder {
	protected long id;
	protected String cardNumber;
	protected String firstName;
	protected String middleName;
	protected String lastName;
	protected String employeeNumber;

    public CardHolder(com.kmware.insystem.model.CardHolder holder) {
        this.id = holder.getId();
    	this.cardNumber = holder.getCardNumber();
    	this.firstName = holder.getFirstName();
    	this.middleName = holder.getMiddleName();
    	this.lastName = holder.getLastName();
    	this.employeeNumber = holder.getEmployeeNumber();
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}


}
