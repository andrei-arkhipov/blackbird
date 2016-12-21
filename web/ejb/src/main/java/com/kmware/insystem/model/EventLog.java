package com.kmware.insystem.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "event_log")
public class EventLog extends BasicFilteredModel implements Serializable {
    private static final long serialVersionUID = 6637536989098672773L;

    private Integer event;
    private Device device;
    private CardHolder cardHolder;
    private Direction direction;
	private static String DOCTYPE = "ELOG";
	
    public EventLog() {
        this.device = new Device();
        this.cardHolder = new CardHolder();
        this.createDate = new Date();
        this.searchField = "";
    }

	@Override
	public String documentType() {
		return DOCTYPE;
	}
    @Column(name = "event", nullable = false)
    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @ManyToOne
    @JoinColumn(name = "cardholder_id", nullable = false)
    public CardHolder getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(CardHolder cardHolder) {
        this.cardHolder = cardHolder;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "direction_id", nullable = false)
    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

	@Override
	public void loadLazyCollections() {
		// TODO Auto-generated method stub
		
	}
}
