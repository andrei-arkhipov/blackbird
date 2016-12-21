package com.kmware.insystem.beans.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.primefaces.model.DualListModel;

import com.kmware.insystem.beans.helper.BasicLazyDataModel;
import com.kmware.insystem.beans.helper.RoleLazyDataModel;
import com.kmware.insystem.dao.BasicDAO;
import com.kmware.insystem.dao.RoleDAO;
import com.kmware.insystem.dao.helper.LazyModelProperties;
import com.kmware.insystem.model.Permission;
import com.kmware.insystem.model.Role;
import com.kmware.insystem.model.RolePermission;

@ManagedBean
@ViewScoped
public class RoleBean extends AbstractViewBean<Role> {
    private static final long serialVersionUID = 4448666592159604373L;

    @Inject
    private RoleDAO dao;

    private String name = null;
    private String internalName = null;
    private Role selectedRole;
    private List<Role> roles;
    private DualListModel<Permission> permDualList;
    private ArrayList<RolePermission> rolesPermissions = new ArrayList<RolePermission>();
    private int count;

    @Override
    @PostConstruct
    public void init() {
        entity = new Role();
        roles = dao.getRoleList();
        this.selectedRole = new Role();
        this.count = 0 ;
    }

    @Override
    public RoleDAO getDAO() {
        return dao;
    }

    @Override
    public Object getEntityId() {
        return entity.getId();
    }

    public void initRolePermissions() {
        if ( count == 0) {
            List<Permission> permissions;

            if ((Long)getEntityId() == -1) {
                permissions = dao.getPermissionsList();
            } else {
                permissions = dao.getPermissionsList2((Long) getEntityId());
            }
            if (permissions != null) {
                permissions.size();
            }
            if (permissions != null && permissions.size() > 0) {
                for (int i = 0, len = permissions.size(); i < len; i++) {
                    RolePermission rp = new RolePermission();
                    rp.setRole(this.entity);
                    rp.setPermission(permissions.get(i));
                    this.entity.getPermissions().add(rp);
                }
            }
        }
        count +=1;
    }

    public void addRole(){
        roles.add(new Role());
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Role getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(Role selectedRole) {
        this.selectedRole = selectedRole;
    }

    public DualListModel<Permission> getPermDualList() {
        List<Permission> source = this.dao.getPermissionsList();
        List<Permission> target = new ArrayList<Permission>();
        this.selectedRole = getDAO().find(this.selectedRole.getId(), true);
        if (this.selectedRole != null && this.selectedRole.getPermissions() != null) {
            target = dao.findPermissionsByRoleId(this.selectedRole.getId());
            Set<Permission> temp = new HashSet<Permission>();
            temp.addAll(target);
            target.clear();
            target.addAll(temp);
            source.removeAll(target);
        }
        this.permDualList = new DualListModel<Permission>(source, target);
        this.entity = this.selectedRole;
        return this.permDualList;
    }

    @SuppressWarnings("unchecked")
	public void setPermDualList(DualListModel<Permission> permDualList) {
        this.selectedRole=this.entity;
        List<Long> stringIds = this.toIdList(permDualList.getTarget().toArray(new String[]{}));
        List<Permission> perms = new ArrayList<Permission>();
        List<Long> ids = new ArrayList<Long>();
        for(Long s:stringIds){
            ids.add(s);
        }
        if (ids.size() > 0) {
            perms = dao.findWhereIdInForPerm(ids);
        }
        List <RolePermission> rolesPermissionsSource = this.selectedRole.getPermissions();
        List <RolePermission> rolesPermissionsTarget = new ArrayList<RolePermission>();
        if(perms.size()>0){
            for(Permission p:perms){
                RolePermission rp = new RolePermission();
                rp.setCanChange(true);
                rp.setCanRead(true);
                rp.setPermission(p);
                rp.setRole(this.selectedRole);
                rolesPermissionsTarget.add(rp);
            }
        }

        rolesPermissions.clear();
        rolesPermissions.addAll(CollectionUtils.subtract(rolesPermissionsSource, rolesPermissionsTarget));
        for (RolePermission rp : rolesPermissions) {
            dao.removeRP(rp);
        }

        rolesPermissions.clear();
        rolesPermissions.addAll(CollectionUtils.subtract(rolesPermissionsTarget, rolesPermissionsSource));
        for (RolePermission rp : rolesPermissions) {
            dao.saveRP(rp);
        }

        this.selectedRole.setPermissions(rolesPermissionsTarget);
        dao.update(this.selectedRole);
        this.permDualList = permDualList;
    }

    @Override
    public String save() {
        if (!validateName()) {
            return "";
        }
        if (!validateInternalName()) {
            return "";
        }
        return super.save();
    }

    @Override
    public String update() {
        if (!validateEditName()) {
            return "";
        }
        if (!validateEditInternalName()) {
            return "";
        }
        return super.update();
    }
    /**
     * Validate role name don't exists match
     * @return true if role name not exists, false otherwise and provide the faces message
     */
    private boolean validateName() {
        if (name != null && name != "") {
        	if (dao.isRoleNameExist(name)) {
                FacesContext context = FacesContext.getCurrentInstance();
                ResourceBundle bundle = ResourceBundle.getBundle("role_messages");
                String errormsg = bundle.getString("validation.name.exist");
                context.addMessage("errors", new FacesMessage(FacesMessage.SEVERITY_ERROR, errormsg, ""));
                context.validationFailed();
                return false;
            }
        }
        entity.setName(name);
        return true;
    }

    private boolean validateEditName() {
        if (name != null && name != "" && !name.equals(entity.getName())) {
        	if (dao.isRoleNameExist(name)) {
                FacesContext context = FacesContext.getCurrentInstance();
                ResourceBundle bundle = ResourceBundle.getBundle("role_messages");
                String errormsg = bundle.getString("validation.name.exist");
                context.addMessage("errors", new FacesMessage(FacesMessage.SEVERITY_ERROR, errormsg, ""));
                context.validationFailed();
                return false;
            }
        }
            entity.setName(name);
            return true;
    }

    /**
     * Validate role name don't exists match
     * @return true if role name not exists, false otherwise and provide the faces message
     */
    private boolean validateInternalName() {
        if (internalName != null && internalName != "") {
        	if (dao.isRoleInternalNameExist(internalName)) {
                FacesContext context = FacesContext.getCurrentInstance();
                ResourceBundle bundle = ResourceBundle.getBundle("role_messages");
                String errormsg = bundle.getString("validation.internal_name.exist");
                context.addMessage("errors", new FacesMessage(FacesMessage.SEVERITY_ERROR, errormsg, ""));
                context.validationFailed();
                return false;
            }
        }
        entity.setInternalName(internalName);
        return true;
    }

    private boolean validateEditInternalName() {
        if (internalName != null && internalName != "" && !internalName.equals(entity.getInternalName())) {
        	if (dao.isRoleInternalNameExist(internalName)) {
                FacesContext context = FacesContext.getCurrentInstance();
                ResourceBundle bundle = ResourceBundle.getBundle("role_messages");
                String errormsg = bundle.getString("validation.internal_name.exist");
                context.addMessage("errors", new FacesMessage(FacesMessage.SEVERITY_ERROR, errormsg, ""));
                context.validationFailed();
                return false;
            }
        }
            entity.setInternalName(internalName);
            return true;
    }

	public String getName() {
        if (entity != null && entity.getName() !=null) 
            name = entity.getName();
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInternalName() {
        if (entity != null && entity.getInternalName() !=null) 
        	internalName = entity.getInternalName();
		return internalName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}

}