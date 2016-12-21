package com.kmware.insystem.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "incorrect_event_log")
public class IncorrectEventLog extends BasicFilteredModel implements java.io.Serializable {
    private static final long serialVersionUID = -464474798045321045L;

    protected String cardNumber;
    private String deviceNumber;
    private IncorrectEvent incorrectEvent;
    private static String DOCTYPE = "ILOG";

    public IncorrectEventLog() {
    	this.incorrectEvent = new IncorrectEvent();
    }

    @Override
    public String documentType() {
        return DOCTYPE;
    }

    @Column(name = "card_number", nullable = false, length = 16)
	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

    @Column(name = "device_number", nullable = false, length = 16)
    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false)
    public IncorrectEvent getIncorrectEvent() {
        return this.incorrectEvent;
    }

    public void setIncorrectEvent(IncorrectEvent incorrectEvent) {
        this.incorrectEvent = incorrectEvent;
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
		IncorrectEventLog other = (IncorrectEventLog) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IncorrectEvent [id=" + id + ", cardNumber=" + cardNumber + ", deviceNumber="
				+ deviceNumber + "]";
	}

	@Override
	public void loadLazyCollections() {

	}

}
