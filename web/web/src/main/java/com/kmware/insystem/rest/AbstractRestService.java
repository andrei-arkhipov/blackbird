package com.kmware.insystem.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.kmware.insystem.dao.BasicDAO;
import com.kmware.insystem.model.BasicModel;

public abstract class AbstractRestService<T extends BasicModel> {
	public static final String ENTITY_PARAM = "entity";

	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<T> listAll() {
		List<T> result = getDAO().getResultList();
		for (T entity : result) {
			cutOffLazyCollections(entity);
		}
		return result;
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public T getJson(@PathParam("id") String id) {
		T entity = getDAO().find(parseId(id));
		cutOffLazyCollections(entity);
		return entity;
	}
	

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response post(T entity) {
		getDAO().save(entity);
		entity = getDAO().find(getId(entity));
		return Response.status(201).build();//entity;
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public T put(T entity) {
		getDAO().update(entity);
		entity = getDAO().find(getId(entity));
		return entity;
	}

	protected abstract BasicDAO<T> getDAO();

	protected abstract Object getId(T entity);

	protected abstract Object parseId(String id);

	protected abstract void cutOffLazyCollections(T entity);
	
}