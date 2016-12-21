package com.kmware.insystem.beans.converters;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang3.StringUtils;

import com.kmware.insystem.model.Organization;

public class OrganizationConverter implements Converter, Serializable  {
    private static final long serialVersionUID = -5515558387583627315L;

    private List<Organization> organizations = Collections.emptyList();

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        if (StringUtils.isNotBlank(arg2)) {
            System.out.println("Organization CONVERTER to object: "+arg2);
            for (Organization c : organizations) {
                if (c.getId() == Long.valueOf(arg2)) {
                    return c;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
        if (arg2 instanceof Organization) {
            System.out.println("Organization CONVERTER to string: "+Long.toString(((Organization) arg2).getId()));
            return Long.toString(((Organization) arg2).getId());
        }
        return "";
    }

}
