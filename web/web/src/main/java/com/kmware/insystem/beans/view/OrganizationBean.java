package com.kmware.insystem.beans.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.kmware.insystem.beans.helper.BasicLazyDataModel;
import com.kmware.insystem.dao.BasicDAO;
import com.kmware.insystem.dao.OrganizationDAO;
import com.kmware.insystem.model.Organization;

@ViewScoped
@ManagedBean
public class OrganizationBean extends AbstractViewBean<Organization> {
    private static final long serialVersionUID = 270657210586652649L;

    @Inject
    private OrganizationDAO dao;

    @Override
    @PostConstruct
    public void init() {
        entity = new Organization();
    }

    @Override
    public BasicDAO<Organization> getDAO() {
        return dao;
    }

    @Override
    public Object getEntityId() {
        return entity.getId();
    }

    @Override
    public BasicLazyDataModel<Organization> getLazyModel() {
        if (lazyModel == null) {
            createLazyModel(new String[] { "name" });
        }
        return lazyModel;
    }

}
