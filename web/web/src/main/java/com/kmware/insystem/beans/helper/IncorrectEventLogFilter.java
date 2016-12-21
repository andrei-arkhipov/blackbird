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

import com.kmware.insystem.beans.converters.IncorrectEventConverter;
import com.kmware.insystem.dao.IncorrectEventDAO;
import com.kmware.insystem.model.IncorrectEvent;
import com.kmware.insystem.model.IncorrectEventLog;

public class IncorrectEventLogFilter implements Serializable {
    private static final long serialVersionUID = 7466172580615424908L;

    protected IncorrectEventLog entity;

    /**
     * Logger to log something if needed
     */
    protected Logger log = Logger.getLogger(getClass());

    protected IncorrectEventDAO incorrectEventDAO;

    protected IncorrectEventConverter incorrectEventConverter;

    protected List<IncorrectEvent> incorrectEvents = null;

    public IncorrectEventLogFilter(IncorrectEventDAO wrong) {
    	incorrectEventDAO = wrong;
        this.init();
    }
	
	
    public void init() {
        log("filter init");
        incorrectEventConverter = new IncorrectEventConverter();
    }

    public List<IncorrectEvent> getAvailableIncorrectEvents() {
        List<IncorrectEvent> result = null;
        if (!isIncorrectEventSelected() && incorrectEvents == null) {
            String dbQuery = "SELECT o FROM IncorrectEvent o ORDER BY o.name ASC";
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("incorrectEvent", entity.getIncorrectEvent());
            result = incorrectEventDAO.getResultList(dbQuery, params);
            if (result == null) {
                result = Collections.emptyList();
            }
            incorrectEvents = result;
            incorrectEventConverter.setIncorrectEvents(result);
        } else {
            result = incorrectEvents;
        }

        return result;
	}

    public boolean isIncorrectEventSelected() {
        return (entity.getIncorrectEvent() != null && entity.getIncorrectEvent().getId() > 0) ? true : false;
    }

    public boolean isFilterComplete() {
        return true;
    }

	public void processIncorrectEventSelection(AjaxBehaviorEvent event) {
        log(entity.getIncorrectEvent().toString());
		entity.setIncorrectEvent(incorrectEventDAO.find(entity.getIncorrectEvent().getId(), true));
        log(" incorrect event reloaded");
	}

    public void clearIncorrectEvent() {
        log(entity.getIncorrectEvent().toString());
        this.entity.setIncorrectEvent(new IncorrectEvent());
        log("Incorrect Event cleared");
        this.incorrectEvents = null;
    }

    public FacesMessage validateIncorrectEvent() {
        ResourceBundle bundle = ResourceBundle.getBundle("wrongeventlog_messages");
        if (!isIncorrectEventSelected()) {
            return new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("validation.device.empty"), "");
        }

        return null;
    }

    /*************** GETTERS/SETTERS *******************/
    public IncorrectEventLog getEntity() {
        return entity;
    }

    public void setEntity(IncorrectEventLog entity) {
        log("entity set");
        this.entity = entity;
    }

    public IncorrectEventConverter getIncorrectEventConverter() {
        return incorrectEventConverter;
    }

    public void setIncorrectEventConverter(IncorrectEventConverter incorrectEventConverter) {
        this.incorrectEventConverter = incorrectEventConverter;
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
