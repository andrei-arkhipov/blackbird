package com.kmware.insystem.beans.reports;

import com.kmware.insystem.dao.CardHolderDAO;
import com.kmware.insystem.dao.DeviceDAO;
import com.kmware.insystem.dao.DirectionDAO;
import com.kmware.insystem.dao.OrganizationDAO;
import com.kmware.insystem.model.Device;
import com.kmware.insystem.model.Direction;
import com.kmware.insystem.model.Organization;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Author: kelevra
 */
@ViewScoped
@ManagedBean(name="eventReportBean")
public class EventReportBean extends BasicReportBean {

    @Inject CardHolderDAO dao;
    @Inject OrganizationDAO oDao;
    @Inject DeviceDAO dDao;
    @Inject DirectionDAO dirDao;

    private Date startDate = new Date();
    private Date endDate = new Date();
    private String cardNumber;
    private String organization;
    private String deviceNumber;
    private String direction;

    @Override
    public String getTemplateFileName() {
        return FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/reports/event_log.jasper");
    }

    @Override
    public String getReportFileName() {
        return "event_log_report_"+startDate.toString()+"_"+endDate.toString();
    }

    @Override
    protected Map<String, Object> processParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        java.sql.Timestamp date_in = new Timestamp(startDate.getTime());
        java.sql.Timestamp date_out = new Timestamp(endDate.getTime());
        params.put("date_in", date_in);
        params.put("date_out", date_out);
        if(cardNumber!=null && !"".equals(cardNumber)) params.put("card_number", cardNumber);
        if(organization!=null && !"".equals(organization)) params.put("org_id", Long.valueOf(organization));
        if(deviceNumber!=null && !"".equals(deviceNumber)) params.put("device_id", Long.valueOf(deviceNumber));
        if(direction!=null && !"".equals(direction)) params.put("direction_id", Long.valueOf(direction));
        return params;
    }

    public List<String> complete(String query) {
        return dao.cardNumbers(query);
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<Organization> getOrganizations() {
        return oDao.getAllOrganizations();
    }

    public List<Device> getDeviceNumbers() {
        return dDao.getAllDeviceNumbers();
    }

    public List<Direction> getDirections() {
        return dirDao.getAllDirections();
    }
}
