package com.kmware.insystem.dao;

import javax.ejb.Stateless;

import com.kmware.insystem.model.Organization;

import java.util.List;

@Stateless
public class OrganizationDAO extends BasicDAO<Organization> {
    private static final long serialVersionUID = 935698951134202801L;

    @SuppressWarnings("unchecked")
	public List<Organization> getAllOrganizations(){
         return (List<Organization>) em.createQuery("select t from Organization t").getResultList();
    }
}
