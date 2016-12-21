package com.kmware.insystem.beans.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.kmware.insystem.beans.helper.BasicLazyDataModel;
import com.kmware.insystem.dao.BasicDAO;
import com.kmware.insystem.dao.DirectionDAO;
import com.kmware.insystem.model.Direction;

@ViewScoped
@ManagedBean
public class DirectionBean extends AbstractViewBean<Direction> {
    private static final long serialVersionUID = 7247369507667487275L;

    @Inject
    private DirectionDAO dao;

    @Override
    @PostConstruct
    public void init() {
        entity = new Direction();
    }

    @Override
    public BasicDAO<Direction> getDAO() {
        return dao;
    }

    @Override
    public Object getEntityId() {
        return entity.getId();
    }

    @Override
    public BasicLazyDataModel<Direction> getLazyModel() {
        if (lazyModel == null) {
            createLazyModel(new String[] { "name", "internalName" });
		}
		return lazyModel;
	}

}
