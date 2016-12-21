package com.kmware.insystem.beans.helper;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.log4j.Logger;

import com.kmware.insystem.beans.converters.CardHolderConverter;
import com.kmware.insystem.beans.converters.DeviceConverter;
import com.kmware.insystem.beans.converters.DirectionConverter;
import com.kmware.insystem.beans.converters.OrganizationConverter;
import com.kmware.insystem.dao.CardHolderDAO;
import com.kmware.insystem.dao.DeviceDAO;
import com.kmware.insystem.dao.DirectionDAO;
import com.kmware.insystem.dao.OrganizationDAO;
import com.kmware.insystem.model.CardHolder;
import com.kmware.insystem.model.Device;
import com.kmware.insystem.model.Direction;
import com.kmware.insystem.model.EventLog;
import com.kmware.insystem.model.Organization;

public class CommonInfoFilter implements Serializable {
    private static final long serialVersionUID = -2312939648829685993L;

    protected EventLog entity;

    /**
     * Logger to log something if needed
     */
    protected Logger log = Logger.getLogger(getClass());

    protected OrganizationDAO organizationDAO;
    protected DeviceDAO deviceDAO;
    protected DirectionDAO directionDAO;
    protected CardHolderDAO cardHolderDAO;

    protected OrganizationConverter organizationConverter;
    protected DeviceConverter deviceConverter;
    protected DirectionConverter directionConverter;
    protected CardHolderConverter cardHolderConverter;

    protected List<Organization> organizations = null;
    protected List<Device> devices = null;
    protected List<Direction> directions = null;
    protected List<CardHolder> cardHolders = null;

    public CommonInfoFilter(OrganizationDAO org, DeviceDAO dev, DirectionDAO dir, CardHolderDAO card) {
        organizationDAO = org;
        deviceDAO = dev;
        directionDAO = dir;
        cardHolderDAO = card;
        this.init();
    }
	
	
    public void init() {
        log("filter init");
        organizationConverter = new OrganizationConverter();
        deviceConverter = new DeviceConverter();
        directionConverter = new DirectionConverter();
        cardHolderConverter = new CardHolderConverter();
    }

    public List<Organization> getAvailableOrganizations() {
        List<Organization> result = null;
        if (!isOrganizationSelected() && organizations == null) {
            String dbQuery = "SELECT o FROM Organization o ORDER BY o.name ASC";
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("device", entity.getDevice());
            result = organizationDAO.getResultList(dbQuery, params);
            if (result == null) {
                result = Collections.emptyList();
            }
            organizations = result;
            organizationConverter.setOrganizations(result);
        } else {
            result = organizations;
        }

        return result;
	}


    public List<Device> getAvailableDevices() {
        List<Device> result = null;
        if (!isDeviceSelected() && devices == null) {
            String dbQuery = "SELECT d FROM Device d ORDER BY d.name ASC";
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("device", entity.getDevice());
            result = deviceDAO.getResultList(dbQuery, params);
            if (result == null) {
                result = Collections.emptyList();
            }
            devices = result;
            deviceConverter.setDevices(result);
        } else {
            result = devices;
        }

        return result;
    }

	public List<Direction> getAvailableDirections() {
		List<Direction> result = null;
		if (!isDirectionSelected() && directions == null) {
			String dbQuery = "SELECT d FROM Direction d WHERE d.id!=3 ORDER BY d.name ASC ";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("device", entity.getDevice());
			result = directionDAO.getResultList(dbQuery, params);
			if (result == null) {
				result = Collections.emptyList();
			}
			directions = result;
			directionConverter.setDirections(result);
		} else {
			result = directions;
		}

		return result;
	}

	public List<CardHolder> getAvailableCardHolders() {
		List<CardHolder> result = null;
		if (!isCardHolderSelected() && cardHolders == null) {
			String dbQuery = "SELECT c FROM CardHolder c ORDER BY c.lastName ASC";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("cardHolder", entity.getCardHolder());
			result = cardHolderDAO.getResultList(dbQuery, params);
			if (result == null) {
				result = Collections.emptyList();
			}
			cardHolders = result;
			cardHolderConverter.setCardHolders(result);
		} else {
			result = cardHolders;
		}

		return result;
	}

    public boolean isOrganizationSelected() {
        return (entity.getDevice().getOrganization() != null && entity.getDevice().getOrganization().getId() > 0) ? true : false;
    }

    public boolean isDeviceSelected() {
        return (entity.getDevice() != null && entity.getDevice().getId() > 0) ? true : false;
    }

    public boolean isDirectionSelected() {
        return (entity.getDevice().getDirection() != null && entity.getDevice().getDirection().getId() > 0) ? true : false;
    }

    public boolean isCardHolderSelected() {
        return (entity.getCardHolder() != null && entity.getCardHolder().getId() > 0) ? true : false;
    }

    public boolean isFilterComplete() {
        return true;
    }

	public void processDeviceSelection(AjaxBehaviorEvent event) {
        log(entity.getDevice().toString());
		entity.setDevice(deviceDAO.find(entity.getDevice().getId(), true));
        log(" device reloaded");
	}

    public void clearOrganization() {
        log(entity.getDevice().getOrganization().toString());
        this.entity.getDevice().setOrganization(new Organization());
        log("Organization cleared");
        this.devices = null;
        this.clearDevice();
    }

    public void clearDevice() {
        log(entity.getDevice().toString());
        this.entity.setDevice(new Device());
        log("Device cleared");
        this.directions = null;
        this.clearDirection();
    }

    public void clearDirection() {
    	log(entity.getDevice().toString());
        this.entity.getDevice().setDirection(new Direction());
        log("Direction cleared");
    }

    public void clearCardHolder() {
    	log(entity.getCardHolder().toString());
        this.entity.setCardHolder(new CardHolder());
        log("CardHolder cleared");
    }

	public FacesMessage validateOrganization() {
        ResourceBundle bundle = ResourceBundle.getBundle("eventlog_messages");
        if (!isOrganizationSelected()) {
            return new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("validation.organization.empty"), "");
        }

        return null;
    }

    public FacesMessage validateDevice() {
        ResourceBundle bundle = ResourceBundle.getBundle("eventlog_messages");
        if (!isDeviceSelected()) {
            return new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("validation.device.empty"), "");
        }

        return null;
    }

    public FacesMessage validateDirection() {
        ResourceBundle bundle = ResourceBundle.getBundle("eventlog_messages");
        if (!isDirectionSelected()) {
            return new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("validation.direction.empty"), "");
        }

        return null;
    }

    public FacesMessage validateCardHolder() {
        ResourceBundle bundle = ResourceBundle.getBundle("eventlog_messages");
        if (!isCardHolderSelected()) {
            return new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("validation.card_holder.empty"), "");
        }

        return null;
    }

    /*************** GETTERS/SETTERS *******************/
    public EventLog getEntity() {
        return entity;
    }

    public void setEntity(EventLog entity) {
        log("entity set");
        this.entity = entity;
    }

    public OrganizationConverter getOrganizationConverter() {
        return organizationConverter;
    }

    public void setOrganizationConverter(OrganizationConverter organizationConverter) {
        this.organizationConverter = organizationConverter;
    }

    public DeviceConverter getDeviceConverter() {
        return deviceConverter;
    }

    public void setDeviceConverter(DeviceConverter deviceConverter) {
        this.deviceConverter = deviceConverter;
    }

    public DirectionConverter getDirectionConverter() {
        return directionConverter;
    }

    public void setDirectionConverter(DirectionConverter directionConverter) {
        this.directionConverter = directionConverter;
    }

    public CardHolderConverter getCardHolderConverter() {
        return cardHolderConverter;
    }

    public void setCardHolderConverter(CardHolderConverter cardHolderConverter) {
        this.cardHolderConverter = cardHolderConverter;
    }

    /**
     * A shortcut for {@link Log#info(Object)}
     * 
     * @param message to log
     */
    protected void log(String message) {
        log.info(message);
    }

}
