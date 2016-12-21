package com.kmware.insystem.beans.converters;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang3.StringUtils;

import com.kmware.insystem.model.IncorrectEvent;

public class IncorrectEventConverter implements Converter, Serializable  {
    private static final long serialVersionUID = -954860439100918483L;

	private List<IncorrectEvent> incorrectEvents = Collections.emptyList();

    public List<IncorrectEvent> getIncorrectEvents() {
        return incorrectEvents;
    }

    public void setIncorrectEvents(List<IncorrectEvent> incorrectEvents) {
        this.incorrectEvents = incorrectEvents;
    }

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        if (StringUtils.isNotBlank(arg2)) {
            System.out.println("IncorrectEvent CONVERTER to object: "+arg2);
            for (IncorrectEvent t : incorrectEvents) {
                if (t.getId() == Long.valueOf(arg2)) {
                    return t;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
        if (arg2 instanceof IncorrectEvent) {
            System.out.println("IncorrectEvent CONVERTER to string: "+Long.toString(((IncorrectEvent) arg2).getId()));
            return Long.toString(((IncorrectEvent) arg2).getId());
        }
        return "";
    }

}
