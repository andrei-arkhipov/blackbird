package com.kmware.insystem.beans.converters;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang3.StringUtils;

import com.kmware.insystem.model.Direction;

public class DirectionConverter implements Converter, Serializable  {
    private static final long serialVersionUID = 7305219525284519735L;

    private List<Direction> directions = Collections.emptyList();

    public List<Direction> getDirections() {
        return directions;
    }

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        if (StringUtils.isNotBlank(arg2)) {
            System.out.println("Direction CONVERTER to object: "+arg2);
            for (Direction t : directions) {
                if (t.getId() == Long.valueOf(arg2)) {
                    return t;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
        if (arg2 instanceof Direction) {
            System.out.println("Direction CONVERTER to string: "+Long.toString(((Direction) arg2).getId()));
            return Long.toString(((Direction) arg2).getId());
        }
        return "";
    }

}
