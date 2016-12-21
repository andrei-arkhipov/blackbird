package com.kmware.insystem.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;

import com.kmware.insystem.dao.BasicDAO;
import com.kmware.insystem.dao.RoleDAO;
import com.kmware.insystem.model.Role;

@Path("/role")
@RequestScoped
public class RoleRestService extends AbstractRestService<Role>{
	
	@Inject
	private RoleDAO dao;

	@Override
	protected BasicDAO<Role> getDAO() {
		return dao;
	}

	@Override
	protected Object getId(Role entity) {
		return entity.getId();
	}

	@Override
	protected Object parseId(String id) {
		return Long.parseLong(id);
	}

	@Override
	protected void cutOffLazyCollections(Role entity) {
        entity.setPermissions(null);
	}

}
