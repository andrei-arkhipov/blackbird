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
import com.kmware.insystem.dao.CardHolderDAO;
import com.kmware.insystem.rest.helper.CardHolder;

@Path("/cardholder")
@RequestScoped
public class CardHolderRestService {

	@Inject
	private CardHolderDAO dao;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public com.kmware.insystem.rest.helper.CardHolder getJson(@PathParam("id") String id) {
		com.kmware.insystem.model.CardHolder holder = dao.find(Long.parseLong(id));
		com.kmware.insystem.rest.helper.CardHolder helper = new com.kmware.insystem.rest.helper.CardHolder(holder);
		return helper;
	}

	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<com.kmware.insystem.rest.helper.CardHolder> listAll() {
		List <com.kmware.insystem.rest.helper.CardHolder> helpers = new ArrayList<com.kmware.insystem.rest.helper.CardHolder>();
		List<com.kmware.insystem.model.CardHolder> result = getDAO().getResultList();
		for (com.kmware.insystem.model.CardHolder entity : result) {
			com.kmware.insystem.rest.helper.CardHolder helper = new com.kmware.insystem.rest.helper.CardHolder(entity);
            helpers.add(helper);
		}
		return helpers;
	}

    

	protected BasicDAO<com.kmware.insystem.model.CardHolder> getDAO() {
		return dao;
	}

	protected Object getId(com.kmware.insystem.model.CardHolder entity) {
		return entity.getId();
	}

}
