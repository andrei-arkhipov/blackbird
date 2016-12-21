package com.kmware.insystem.dao;

import javax.ejb.Stateless;

import com.kmware.insystem.model.Direction;

import java.util.List;

@Stateless
public class DirectionDAO extends BasicDAO<Direction> {
    private static final long serialVersionUID = 6561543348198047321L;

    @SuppressWarnings("unchecked")
	public List<Direction> getAllDirections(){
        return (List<Direction>) em.createQuery("select t from Direction t where t.internalName = 'in' or t.internalName = 'out'").getResultList();
    }
}