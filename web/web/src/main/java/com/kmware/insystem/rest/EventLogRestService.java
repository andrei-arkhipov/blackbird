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
import com.kmware.insystem.dao.EventLogDAO;
import com.kmware.insystem.model.EventLog;
import com.kmware.insystem.rest.helper.EventLogRestHelper;

@Path("/eventlog")
@RequestScoped
public class EventLogRestService {

    @Inject
    private EventLogDAO dao;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public EventLogRestHelper getJson(@PathParam("id") String id) {
		EventLog holder = dao.find(Long.parseLong(id));
		EventLogRestHelper helper = new EventLogRestHelper(holder);
		return helper;
	}

	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<EventLogRestHelper> listAll() {
		List <EventLogRestHelper> helpers = new ArrayList<EventLogRestHelper>();
		List<EventLog> result = getDAO().getResultList();
		for (EventLog entity : result) {
			EventLogRestHelper helper = new EventLogRestHelper(entity);
            helpers.add(helper);
		}
		return helpers;
	}

	protected BasicDAO<EventLog> getDAO() {
		return dao;
	}

	protected Object getId(EventLog entity) {
		return entity.getId();
	}
}
