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
import com.kmware.insystem.dao.DeviceDAO;
import com.kmware.insystem.model.Device;

@Path("/device")
@RequestScoped
public class DeviceRestService {

    @Inject
    private DeviceDAO dao;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public com.kmware.insystem.rest.helper.Device getJson(@PathParam("id") String id) {
		com.kmware.insystem.model.Device holder = dao.find(Long.parseLong(id));
		com.kmware.insystem.rest.helper.Device helper = new com.kmware.insystem.rest.helper.Device(holder);
		return helper;
	}

	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<com.kmware.insystem.rest.helper.Device> listAll() {
		List <com.kmware.insystem.rest.helper.Device> helpers = new ArrayList<com.kmware.insystem.rest.helper.Device>();
		List<com.kmware.insystem.model.Device> result = getDAO().getResultList();
		for (com.kmware.insystem.model.Device entity : result) {
			com.kmware.insystem.rest.helper.Device helper = new com.kmware.insystem.rest.helper.Device(entity);
            helpers.add(helper);
		}
		return helpers;
	}

	protected BasicDAO<Device> getDAO() {
		return dao;
	}

	protected Object getId(Device entity) {
		return entity.getId();
	}
}
