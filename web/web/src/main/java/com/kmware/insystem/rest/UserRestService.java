package com.kmware.insystem.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;

import com.kmware.insystem.dao.BasicDAO;
import com.kmware.insystem.dao.UserDAO;
import com.kmware.insystem.model.User;

@Path("/user")
@RequestScoped
public class UserRestService extends AbstractRestService<User>{
	
	@Inject
	private UserDAO dao;

	@Override
	protected BasicDAO<User> getDAO() {
		return dao;
	}

	@Override
	protected Object getId(User entity) {
		return entity.getId();
	}

	@Override
	protected Object parseId(String id) {
		return Long.parseLong(id);
	}

	@Override
	protected void cutOffLazyCollections(User entity) {
        entity.setRoles(null);
	}

}
