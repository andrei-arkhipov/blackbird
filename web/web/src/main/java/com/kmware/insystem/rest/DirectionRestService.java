package com.kmware.insystem.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;

import com.kmware.insystem.dao.BasicDAO;
import com.kmware.insystem.dao.DirectionDAO;
import com.kmware.insystem.model.Direction;

@Path("/direction")
@RequestScoped
public class DirectionRestService extends AbstractRestService<Direction>{
	
	@Inject
	private DirectionDAO dao;

	@Override
	protected BasicDAO<Direction> getDAO() {
		return dao;
	}

	@Override
	protected Object getId(Direction entity) {
		return entity.getId();
	}

	@Override
	protected Object parseId(String id) {
		return Long.parseLong(id);
	}

	@Override
	protected void cutOffLazyCollections(Direction entity) {
		// TODO Auto-generated method stub		
	}

}
