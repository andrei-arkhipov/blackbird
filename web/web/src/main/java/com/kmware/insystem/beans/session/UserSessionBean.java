package com.kmware.insystem.beans.session;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.Context;
import javax.security.auth.Subject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jboss.security.SecurityContextAssociation;
import org.jboss.security.SimpleGroup;

import com.kmware.insystem.beans.helper.IValueHolder;
import com.kmware.insystem.dao.RoleDAO;
import com.kmware.insystem.dao.UserDAO;
import com.kmware.insystem.model.Role;
import com.kmware.insystem.model.RolePermission;
import com.kmware.insystem.model.User;

@Named("userSession")
@javax.enterprise.context.SessionScoped
public class UserSessionBean extends AbstractSessionBean implements Serializable, IValueHolder {
    private static final long serialVersionUID = 1808138085288610475L;

    private static final String PROPERTIES_NAME = "ldap";
    private static final String ROLES_QUERY = "query.roles";
    private static final String ROLES_QUERY_PARAM = "query.roles.parameter";

    @Inject
    private UserDAO userDAO;

    @Inject
    private RoleDAO roleDAO;

    private User user;
    private RolePermission lastPermissions;
    private List<Role> roles = new ArrayList<Role>();

    private Map<Object, Object> values;
    private ResourceBundle ldapProperties;

    @Override
    @PostConstruct
    public void init() {
        log("User session created");
//        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Kiev"));
        user = new User();
        lastPermissions = null;
        Role role = new Role();
        roles.add(role);
        user.setRoles(roles);

        ldapProperties = ResourceBundle.getBundle(PROPERTIES_NAME);
        log("Ldap properties loaded? -> " + ldapProperties.containsKey(Context.INITIAL_CONTEXT_FACTORY));
        login();
    }

    public boolean isLoggedIn() {
//        if (values != null)
//            System.out.println("session values = " + values.toString());
//        else
//            System.out.println("values empty");

        boolean result = (user!= null && StringUtils.isNotBlank(user.getUserName())) ? true : false;
        return result;
    }

    public void login() {
        if (isLoggedIn()) {
            return;
        }

        Subject caller = SecurityContextAssociation.getSubject();

        System.out.println("caller = " +caller);
        // FacesContext.getCurrentInstance().
        if (caller != null && (user.getUserName() == null || "".equals(user.getUserName()))) {
            boolean first = false;
            List<String> roles = new ArrayList<String>();
            for (Principal p : caller.getPrincipals()) {
                if (!first) {
                    user.setUserName(p.getName());
                    first = true;
                    continue;
                } else if ("Roles".equals(p.getName())) {
                    SimpleGroup rolesInAD = (SimpleGroup) p;
                    Enumeration<Principal> roleEnum = rolesInAD.members();
                    for (Enumeration<Principal> r = roleEnum; r.hasMoreElements();) {
                        roles.add(r.nextElement().getName());
                    }
                }
            }

            // Set Roles
            if (user.getUserName() != null && !"".equals(user.getUserName()) && !roles.isEmpty()) {
                String query = ldapProperties.getString(ROLES_QUERY);
                Map<String, Object> params = new HashMap<String, Object>();
                params.put(ldapProperties.getString(ROLES_QUERY_PARAM), roles);
                List<Role> userRoles = roleDAO.getResultList(query, params);
                this.user.setRoles(userRoles);
            }


            try {
                User acc = this.userDAO.findByUserName(this.user.getUserName(), true);
                // we are logged through database
                if ((user.getFirstName() == null || user.getFirstName().equals("")) && acc != null) {
                    this.user = acc;
                }
            } catch (Exception e) {
                log("No user found,sorry. " + e.getMessage());
            }
            if (!user.getIsActive()) {
                SecurityContextAssociation.clearSecurityContext();
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ExternalContext externalContext = facesContext.getExternalContext();
                HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();
                req.getSession().invalidate();
                this.user = new User();
                try {
                    req.getRequestDispatcher("/blocked.jsf").forward(req, (HttpServletResponse) externalContext.getResponse());
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (user != null) {
            System.out.println(user.getFirstName());
        }

	}// end login

    public String logout() {
        SecurityContextAssociation.clearSecurityContext();

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();
        req.getSession().invalidate();
        this.user = new User();
        System.out.println(req.getServletPath() + "?faces-redirect=true");
        return "/index.jsf?faces-redirect=true";
    }

    protected List<Role> getRoles(Subject caller) {
        return null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserFullName(){
        return user.getLastName()+" "+user.getFirstName()+" "+user.getMiddleName();
    }

    public String getUserNameWithInitials(){
        return user.getLastName()+" "+(StringUtils.isNotBlank(user.getFirstName()) ? user.getFirstName().charAt(0)+". " : "")
                + (StringUtils.isNotBlank(user.getMiddleName()) ? user.getMiddleName().charAt(0)+".":"" ) ;
    }

    @Override
    public Object getValueFor(Object o) {
        return values.get(o);
    }

    @Override
    public void setValueFor(Object o, Object v) {
        values.put(o, v);
	}

    public boolean hasRole(String roleName) {
        for (Role role : user.getRoles()) {
            if (role.getName().equals(roleName)) {
                return true;
            }
        }
        return false;
    }

    public boolean canRead(String roleName) {
        if (!isLoggedIn()) {
            return false;
        }
        if (this.lastPermissions != null) {
            if (roleName.equals(this.lastPermissions.getPermission().getName() + "")) {
                if (this.lastPermissions.getCanRead()) {
                    return this.lastPermissions.getCanRead();
                }
            }
        }

        List<Role> roles = this.user.getRoles();
        for (Role role : roles) {
            List<RolePermission> rolePermissions = role.getPermissions();
            for (RolePermission roleRolePermission : rolePermissions) {
                if (roleName.equals(roleRolePermission.getPermission().getName() + "")) {
                    if (roleRolePermission.getCanRead()) {
                        this.lastPermissions = roleRolePermission;
                        return true;
                    } else {
                        break;
                    }
                }
            }
        }
        return false;
    }

	public boolean canRead2(String roleInternalName) {
		if(!isLoggedIn()){
			return false;
		}
		if (this.lastPermissions != null) {
			if (roleInternalName.equals(this.lastPermissions.getPermission()
					.getInternalName() + "")) {
				if (this.lastPermissions.getCanRead()) {
					return this.lastPermissions.getCanRead();
				}
			}
		}

		List<Role> roles = this.user.getRoles();
		for (Role role : roles) {
			List<RolePermission> rolePermissions = role
					.getPermissions();
			for (RolePermission roleRolePermission : rolePermissions) {
				if (roleInternalName.equals(roleRolePermission.getPermission()
						.getInternalName() + "")) {
					if (roleRolePermission.getCanRead()) {
						this.lastPermissions = roleRolePermission;
						return true;
					} else {
						break;
					}
				}
			}
		}
		return false;
	}

    public boolean canChange(String roleName) {
        if (!isLoggedIn()) {
            return false;
        }
        if (this.lastPermissions != null) {
            if (roleName.equals(this.lastPermissions.getPermission().getName() + "")) {
                return this.lastPermissions.getCanChange();
            }
        }

        List<Role> roles = this.user.getRoles();
        for (Role role : roles) {
            List<RolePermission> rolePermissions = role.getPermissions();
            for (RolePermission roleRolePermission : rolePermissions) {
                if (roleName.equalsIgnoreCase(roleRolePermission.getPermission().getName() + "")) {
                    if (roleRolePermission.getCanChange()) {
                        this.lastPermissions = roleRolePermission;
                        return true;
                    } else {
                        break;
                    }
                }
            }
        }
        return false;
    }

	// Check by the "internal name" of the role (which schouldn't be changed)
	public boolean canChange2(String roleInternalName) {
		if(!isLoggedIn()){
			return false;
		}
		if (this.lastPermissions != null) {
			if (roleInternalName.equals(this.lastPermissions.getPermission()
					.getInternalName() + "")) {
				return this.lastPermissions.getCanChange();
			}
		}

		List<Role> roles = this.user.getRoles();
		for (Role role : roles) {
			List<RolePermission> rolePermissions = role
					.getPermissions();
			for (RolePermission roleRolePermission : rolePermissions) {
				if (roleInternalName.equals(roleRolePermission.getPermission().getInternalName() + "")) {
					if (roleRolePermission.getCanChange()) {
						this.lastPermissions = roleRolePermission;
						return true;
					} else {
						break;
					}
				}
			}
		}
		return false;
	}
}
