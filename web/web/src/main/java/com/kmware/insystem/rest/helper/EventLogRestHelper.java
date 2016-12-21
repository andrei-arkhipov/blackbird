package com.kmware.insystem.rest.helper;

import java.util.Date;

import com.kmware.insystem.model.Direction;
import com.kmware.insystem.model.EventLog;

public class EventLogRestHelper {
	private long id;
    private Integer event;
    private Direction direction;
    private Device device;
    private CardHolder cardHolder;
    private Date createDate;

	public EventLogRestHelper(EventLog holder) {
        this.id = holder.getId();
        this.cardHolder = new CardHolder(holder.getCardHolder());
        this.createDate = holder.getCreateDate();
        this.event = holder.getEvent();
        this.device = new Device(holder.getDevice());
        this.direction = holder.getDirection();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getEvent() {
		return event;
	}

	public void setEvent(Integer event) {
		this.event = event;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

    public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public CardHolder getCardHolder() {
		return cardHolder;
	}

	public void setCardHolder(CardHolder cardHolder) {
		this.cardHolder = cardHolder;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
