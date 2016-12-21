package com.kmware.insystem.beans.converters;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang3.StringUtils;

import com.kmware.insystem.model.Device;


public class DeviceConverter implements Converter, Serializable  {
    private static final long serialVersionUID = 3965540852136633468L;

    private List<Device> devices = Collections.emptyList();

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        if (StringUtils.isNotBlank(arg2)) {
            System.out.println("Device CONVERTER to object: "+arg2);
            for (Device t : devices) {
                if (t.getId() == Long.valueOf(arg2)) {
                    return t;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
        if (arg2 instanceof Device) {
            System.out.println("Device CONVERTER to string: "+Long.toString(((Device) arg2).getId()));
            return Long.toString(((Device) arg2).getId());
        }
        return "";
    }

}
