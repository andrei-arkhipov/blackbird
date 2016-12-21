package com.kmware.insystem.beans.view;

import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.kmware.insystem.beans.helper.BasicLazyDataModel;
import com.kmware.insystem.dao.BasicDAO;
import com.kmware.insystem.dao.DeviceDAO;
import com.kmware.insystem.dao.DirectionDAO;
import com.kmware.insystem.dao.OrganizationDAO;
import com.kmware.insystem.model.Device;
import com.kmware.insystem.model.Direction;
import com.kmware.insystem.model.Organization;

@ViewScoped
@ManagedBean
public class DeviceBean extends AbstractViewBean<Device> {
    private static final long serialVersionUID = 899751519103314019L;

    @Inject
    private DeviceDAO deviceDAO;
    
    @Inject
    private OrganizationDAO organizationDAO;

    @Inject
    private DirectionDAO directionDAO;

    private String deviceNumber;
    private List<Organization> organizations;
    private List<Direction> directions;

    @Override
    @PostConstruct
    public void init() {
        this.entity = new Device();
    }

    @Override
    public BasicDAO<Device> getDAO() {
        return deviceDAO;
    }

    @Override
    public Object getEntityId() {
        return this.entity.getId();
    }

    @Override
    public String save() {
        if (!validateLogin()) {
            return "";
        }

        return super.save();
    }

    @Override
    public String update() {
        if (!validateEditLogin()) {
            return "";
        }

        return super.update();
    }

    /**
     * Validate device number don't exists match
     * @return true if device number not exists, false otherwise and provide the faces message
     */
    private boolean validateLogin() {
        if (deviceNumber != null && deviceNumber != "") {
        	if (deviceDAO.isDeviceNumberExist(deviceNumber)) {
                FacesContext context = FacesContext.getCurrentInstance();
                ResourceBundle bundle = ResourceBundle.getBundle("device_messages");
                String errormsg = bundle.getString("validation.device_number.exist");
                context.addMessage("errors", new FacesMessage(FacesMessage.SEVERITY_ERROR, errormsg, ""));
                context.validationFailed();
                return false;
            }
        }
        entity.setDeviceNumber(deviceNumber);
        return true;
    }

    private boolean validateEditLogin() {
        if (deviceNumber != null && deviceNumber != "" && !deviceNumber.equals(entity.getDeviceNumber())) {
        	if (deviceDAO.isDeviceNumberExist(deviceNumber)) {
                FacesContext context = FacesContext.getCurrentInstance();
                ResourceBundle bundle = ResourceBundle.getBundle("device_messages");
                String errormsg = bundle.getString("validation.device_number.exist");
                context.addMessage("errors", new FacesMessage(FacesMessage.SEVERITY_ERROR, errormsg, ""));
                context.validationFailed();
                return false;
            }
        }
            entity.setDeviceNumber(deviceNumber);
            return true;
    }

    
    public String getDeviceNumber() {
        if (entity != null && entity.getDeviceNumber() !=null) 
        	deviceNumber = entity.getDeviceNumber();
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public List<Organization> getOrganizations() {
        if (organizations == null) {
            organizations = organizationDAO.getResultList();
        }

        return organizations;
    }

    public List<Direction> getDirections() {
        if (directions == null) {
        	directions = directionDAO.getResultList();
        }

        return directions;
    }

    @Override
    protected void preSave() {
        entity.setOrganization(organizationDAO.find(entity.getOrganization().getId()));
        entity.setDirection(directionDAO.find(entity.getDirection().getId()));

	}

    @Override
    public BasicLazyDataModel<Device> getLazyModel() {
        if (lazyModel == null) {
            createLazyModel(new String[] { "deviceNumber", "organization.name", "direction.name" });
        }

        return lazyModel;
    }

}
