package com.kmware.insystem.beans.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.kmware.insystem.beans.helper.BasicLazyDataModel;
import com.kmware.insystem.dao.BasicDAO;
import com.kmware.insystem.dao.IncorrectEventDAO;
import com.kmware.insystem.model.IncorrectEvent;


@ViewScoped
@ManagedBean
public class IncorrectEventBean extends AbstractViewBean<IncorrectEvent> {
    private static final long serialVersionUID = 7247369507667487275L;

    @Inject
    private IncorrectEventDAO dao;

    @Override
    @PostConstruct
    public void init() {
        entity = new IncorrectEvent();
    }

    @Override
    public BasicDAO<IncorrectEvent> getDAO() {
        return dao;
    }

    @Override
    public Object getEntityId() {
        return entity.getId();
    }

    @Override
    public BasicLazyDataModel<IncorrectEvent> getLazyModel() {
        if (lazyModel == null) {
            createLazyModel(new String[] { "name", "internalName" });
		}
		return lazyModel;
	}

}
