package com.kmware.insystem.beans.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.kmware.insystem.beans.helper.BasicExtendedFilterManager;
import com.kmware.insystem.dao.BasicDAO;
import com.kmware.insystem.dao.EventLogDAO;
import com.kmware.insystem.model.EventLog;

@ViewScoped
@ManagedBean
public class EventLogBean extends AbstractFilteredBean<EventLog> {
    private static final long serialVersionUID = 8425035183082152599L;

    @Inject
    private EventLogDAO dao;

    private Integer documentToProcess;
    private List<String> selectedDocuments = new ArrayList<String>();
    private BasicExtendedFilterManager<EventLog> basicExtendedFilterManager;

    @Override
    @PostConstruct
    public void init() {
        super.init();
        FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        log("EventLog bean init");
        entity = new EventLog();
        filter.setEntity(this.entity);
        basicExtendedFilterManager = new BasicExtendedFilterManager<EventLog>(getDAO(), userSession.getUser(),entity.documentType());
    }
	
    @Override
    public BasicDAO<EventLog> getDAO() {
        return dao;
    }

    public List<String> getSelectedDocuments() {
        return selectedDocuments;
    }

    public void setSelectedDocuments(List<String> selectedDocuments) {
        this.selectedDocuments = selectedDocuments;
    }

    @Override
    protected boolean validationOk() {
        boolean ok = super.validationOk();
        FacesContext fc = FacesContext.getCurrentInstance();
        List<FacesMessage> errors = new ArrayList<FacesMessage>();
        FacesMessage fm = filter.validateOrganization();
        if (fm != null) {
            errors.add(fm);
        }
        fm = filter.validateDevice();
        if (fm != null) {
            errors.add(fm);
        }
        fm = filter.validateDirection();
        if (fm != null) {
            errors.add(fm);
        }
        fm = filter.validateCardHolder();
        if (fm != null) {
            errors.add(fm);
        }

        if (errors.size() > 0) {
            ok = false;
        }
        if (!ok) {
            for (FacesMessage facesMessage : errors) {
                fc.addMessage(null, facesMessage);
            }
            fc.validationFailed();
        }
        return ok;
    }
	
    @Override
    protected void preSave() {
        super.preSave();
    }

    @Override
    public String update() {
        String result =  super.update();
        if("".equals(result)){
            return "";
        }
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BasicExtendedFilterManager<EventLog> getFilterManager() {
        return basicExtendedFilterManager;
    }

    @Override
    public void setFilterManager(BasicExtendedFilterManager<EventLog> extendedFilterManager) {
        this.basicExtendedFilterManager = extendedFilterManager;
    }

    public Integer getDocumentToProcess() {
        return documentToProcess;
    }

    public void setDocumentToProcess(Integer documentToProcess) {
        this.documentToProcess = documentToProcess;
    }

    @Override
    public void processUrlParameters() {
        super.processUrlParameters();
	}
	
    public void clearOrganization(){
        filter.clearOrganization();
        selectedDocuments.clear();
    }

    public void clearDevice(){
        filter.clearDevice();
        selectedDocuments.clear();
    }

    public void clearDirection(){
        filter.clearDirection();
        selectedDocuments.clear();
    }

    public void clearCardHolder(){
        filter.clearCardHolder();
        selectedDocuments.clear();
    }

}