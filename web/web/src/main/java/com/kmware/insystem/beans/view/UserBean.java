package com.kmware.insystem.beans.view;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.DualListModel;

import com.kmware.insystem.beans.helper.BasicLazyDataModel;
import com.kmware.insystem.beans.helper.Crypt;
import com.kmware.insystem.dao.BasicDAO;
import com.kmware.insystem.dao.RoleDAO;
import com.kmware.insystem.dao.UserDAO;
import com.kmware.insystem.model.Role;
import com.kmware.insystem.model.User;

@ManagedBean
@ViewScoped
public class UserBean extends AbstractViewBean<User> {
    private static final long serialVersionUID = 3607656527681772698L;

    @Inject
    private UserDAO dao;

    @Inject
    private RoleDAO roleDao;

    private DualListModel<Role> rolesDualListModel;

    private String login = null;
    private String password = null;
    private String passwordRepeat = null;

    @Override
    @PostConstruct
    public void init() {
        entity = new User();
    }

    @Override
    public BasicDAO<User> getDAO() {
        return dao;
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
        if (!validatePassword()) {
            return "";
        }
        return super.save();
    }

    @Override
    public String update() {
        if (!validateEditLogin()) {
            return "";
        }
        if (!validateSkipPassword()) {
            return "";
        }
        return super.update();
    }

    @Override
    protected void preSave() {
        super.preSave();
        List<Long> ids = toIdList(rolesDualListModel.getTarget().toArray(new String[] {}));
        List<Role> roles = new ArrayList<Role>();
        if (ids.size() > 0) {
            roles = roleDao.findWhereIdIn(ids);
        }
        roles.add(roleDao.getRoleByName("jboss"));
        this.entity.setRoles(roles);
    }
	
    /**
     * Validate the passwords match
     * @return true if passwords match, false otherwise and provide the faces message
     */
    public boolean validatePassword(){
        if(password != null && !"".equals(password.trim())
           && passwordRepeat != null && !"".equals(passwordRepeat.trim())
           && StringUtils.equals(password, passwordRepeat)){
            if (getPassword().length() < 5 || getPassword().length() > 15) {
                FacesContext context = FacesContext.getCurrentInstance();
                ResourceBundle bundle = ResourceBundle.getBundle("user_messages");
                String errormsg = bundle.getString("validation.password.length");
                context.addMessage("errors", new FacesMessage(FacesMessage.SEVERITY_ERROR, errormsg, ""));
                context.validationFailed();
                return false;
            }
            this.entity.setPassword(Crypt.encrypt(password));
            return true;
        }
        else {
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle bundle = ResourceBundle.getBundle("user_messages");
            String errormsg = bundle.getString("validation.password.not.match");
            context.addMessage("errors", new FacesMessage(FacesMessage.SEVERITY_ERROR, errormsg, ""));
            context.validationFailed();
            return false;
        }
	}

    public boolean validateSkipPassword(){
    	if (entity.getPassword() != null
                && "".equals(password.trim())) {
            return true;
    	}
    	else {
         	if (getPassword().length() < 5 || getPassword().length() > 15) {
                 FacesContext context = FacesContext.getCurrentInstance();
                 ResourceBundle bundle = ResourceBundle.getBundle("user_messages");
                 String errormsg = bundle.getString("validation.password.length");
                 context.addMessage("errors", new FacesMessage(FacesMessage.SEVERITY_ERROR, errormsg, ""));
                 context.validationFailed();
                 return false;
             }
         	return validatePassword();
    	}
    }

    /**
     * Validate users don't exists match
     * @return true if users not exists, false otherwise and provide the faces message
     */
    private boolean validateLogin() {
        if (login != null && login != "") {
        	if (dao.isUserExist(login)) {
                FacesContext context = FacesContext.getCurrentInstance();
                ResourceBundle bundle = ResourceBundle.getBundle("user_messages");
                String errormsg = bundle.getString("validation.name.exist");
                context.addMessage("errors", new FacesMessage(FacesMessage.SEVERITY_ERROR, errormsg, ""));
                context.validationFailed();
                return false;
            }
        }
        entity.setUserName(login);
        return true;
    }

    private boolean validateEditLogin() {
        if (login != null && login != "" && !login.equals(entity.getUserName())) {
        	if (dao.isUserExist(login)) {
                FacesContext context = FacesContext.getCurrentInstance();
                ResourceBundle bundle = ResourceBundle.getBundle("user_messages");
                String errormsg = bundle.getString("validation.name.exist");
                context.addMessage("errors", new FacesMessage(FacesMessage.SEVERITY_ERROR, errormsg, ""));
                context.validationFailed();
                return false;
            }
        }
            entity.setUserName(login);
            return true;
    }

    public DualListModel<Role> getRolesDualListModel() {
        if (rolesDualListModel == null && StringUtils.isNotBlank(entity.getUserName())) {
            List<Role> source = roleDao.getResultList();
            if (source.indexOf(roleDao.getRoleByName("jboss")) >= 0 )
                source.remove(source.indexOf(roleDao.getRoleByName("jboss")));
            List<Role> dest = entity.getRoles();
            if (dest.indexOf(roleDao.getRoleByName("jboss")) >= 0) {
                dest.remove(dest.indexOf(roleDao.getRoleByName("jboss")));
            }
            source.removeAll(dest);
            rolesDualListModel = new DualListModel<Role>(source, dest);
        } else if (rolesDualListModel == null) {
            List<Role> source = roleDao.getResultList();
            if (source.indexOf(roleDao.getRoleByName("jboss")) >= 0 )
                source.remove(source.indexOf(roleDao.getRoleByName("jboss")));
            List<Role> dest = new ArrayList<Role>();
            rolesDualListModel = new DualListModel<Role>(source, dest);
        }
        return rolesDualListModel;
    }

    public void setRolesDualListModel(DualListModel<Role> rolesDualListModel) {
        this.rolesDualListModel = rolesDualListModel;
    }

    @Override
    public BasicLazyDataModel<User> getLazyModel() {
        if (lazyModel == null) {
            createLazyModel(new String[] { "userName", "email", "firstName", "middleName", "lastName" });
        }
        return lazyModel;
    }

   public String getLogin() {
        if (entity != null && entity.getUserName() !=null) 
            login = entity.getUserName();
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	 public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordRepeat() {
		return passwordRepeat;
	}

	public void setPasswordRepeat(String passwordRepeat) {
		this.passwordRepeat = passwordRepeat;
	}

}