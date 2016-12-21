package com.kmware.insystem.beans.converters;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang3.StringUtils;

import com.kmware.insystem.model.Permission;

public class PermissionConverter implements Converter, Serializable  {
    private static final long serialVersionUID = -5515558387583627315L;

    private List<Permission> Permissions = Collections.emptyList();

    public List<Permission> getPermissions() {
        return Permissions;
    }

    public void setPermissions(List<Permission> Permissions) {
        this.Permissions = Permissions;
    }

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        if (StringUtils.isNotBlank(arg2)) {
            System.out.println("Permission CONVERTER to object: "+arg2);
            for (Permission c : Permissions) {
                if (c.getId() == Long.valueOf(arg2)) {
                    return c;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
        if (arg2 instanceof Permission) {
            System.out.println("Permission CONVERTER to string: "+Long.toString(((Permission) arg2).getId()));
            return Long.toString(((Permission) arg2).getId());
        }
        return "";
    }

}
