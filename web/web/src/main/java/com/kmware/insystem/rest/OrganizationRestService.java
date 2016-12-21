package com.kmware.insystem.rest;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.kmware.insystem.dao.BasicDAO;
import com.kmware.insystem.dao.OrganizationDAO;

@Path("/organization")
@RequestScoped
public class OrganizationRestService {

    @Inject
    private OrganizationDAO dao;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public com.kmware.insystem.rest.helper.Organization getJson(@PathParam("id") String id) {
		com.kmware.insystem.model.Organization holder = dao.find(Long.parseLong(id));
		com.kmware.insystem.rest.helper.Organization helper = new com.kmware.insystem.rest.helper.Organization(holder);
		return helper;
	}

	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<com.kmware.insystem.rest.helper.Organization> listAll() {
		List <com.kmware.insystem.rest.helper.Organization> helpers = new ArrayList<com.kmware.insystem.rest.helper.Organization>();
		List<com.kmware.insystem.model.Organization> result = getDAO().getResultList();
		for (com.kmware.insystem.model.Organization entity : result) {
			com.kmware.insystem.rest.helper.Organization helper = new com.kmware.insystem.rest.helper.Organization(entity);
            helpers.add(helper);
		}
		return helpers;
	}

	protected BasicDAO<com.kmware.insystem.model.Organization> getDAO() {
		return dao;
	}

	protected Object getId(com.kmware.insystem.model.Organization entity) {
		return entity.getId();
	}
}
