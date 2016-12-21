package com.kmware.insystem.beans.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.transaction.RollbackException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.exception.DataException;

import com.kmware.insystem.beans.helper.BasicLazyDataModel;
import com.kmware.insystem.beans.session.SessionDataBean;
import com.kmware.insystem.dao.BasicDAO;
import com.kmware.insystem.dao.helper.LazyModelProperties;
import com.kmware.insystem.model.BasicModel;

/**
 * Represents the common {@link ViewScoped} bean of the application
 * 
 * @param <T>
 *            entity class parameter
 */
public abstract class AbstractViewBean<T extends BasicModel> implements Serializable {
    private static final long serialVersionUID = -4343760562982768769L;

    /**
     * Used to store something if needed to pass between views etc.
     */
    @Inject
    protected SessionDataBean sessionData;

    /**
     * Lazy model for entity class
     */
    protected BasicLazyDataModel<T> lazyModel;

    /**
     * Entity instance
     */
    protected T entity;

    /**
     * Logger to log something if needed
     */
    protected Logger log = Logger.getLogger(getClass());
	
    /**
     * The abstract method to be implemented. It's recommended to initialize
     * your entity here rather than anywhere else. When implemented must be
     * annotated with {@link PostConstruct}
     */
    public abstract void init();

    /**
     * Method used in view and edit jsf pages to retrieve the entity using given
     * id.Called on <b>preRenderView</b> event of the jsf pages.
     */
    public void processUrlParameters() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            String idParam = ((String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            if (StringUtils.isNotBlank(idParam)) {
                this.entity = getEntityById(idParam);
            }
        }
    }

    /**
     * Retrieves entity by id (defaults to Long value). Must be overridden if
     * entity's id is other than Long value
     * 
     * @param idParam string representation of id
     *
     * @return entity found in database
     */
    protected T getEntityById(String idParam) {
         return getDAO().find(Long.valueOf(idParam), true);
    }

    /**
     * Get the dao for current implementation of the bean. You can create dao in
     * many ways for e.g. injecting one and returning it in this method. This
     * method is used in {@link AbstractViewBean} for implementing some common
     * methods that are used in further implementations
     * 
     * @return dao instance
     */
    public abstract BasicDAO<T> getDAO();

    /**
     * Used in {@link AbstractViewBean#update()},
     * {@link AbstractViewBean#cancelEdit()} to pass a valid entity id
     * 
     * @return entity id
     */
    public abstract Object getEntityId();

    /**
     * Save the entity and navigate to list page
     * 
     * @return outcome "list.jsf?faces-redirect=true"
     */
    public String save() {
        try{
            preSave();
            getDAO().save(entity);
        } catch(EJBException ex){
            if(ex.getCause() instanceof RollbackException){
                if(ex.getCause().getCause() instanceof PersistenceException && ex.getCause().getCause().getCause() instanceof DataException){
                    return handleFieldLengthExceeded();
                }
            }
        }

        return "list.jsf?faces-redirect=true";
    }

    /**
     * Update the entity and navigate to view page
     * 
     * @return outcome "view.jsf?faces-redirect=true&id={id}"
     */
    public String update() {
        try {
            preSave();
            getDAO().update(entity);
        } catch (EJBException ex) {
            if (ex.getCause() instanceof OptimisticLockException) {				
                return handleOptimisticLock();
            }
            if(ex.getCause() instanceof RollbackException){
                if(ex.getCause().getCause() instanceof PersistenceException && ex.getCause().getCause().getCause() instanceof DataException){
                    return handleFieldLengthExceeded();
                }
            }
        }

        return "view.jsf?faces-redirect=true&id=" + getEntityId();
	}

    /**
     * Begin the creation of new entity
     * 
     * @return outcome "new.jsf?faces-redirect=true"
     */
    public String toNew() {
        return "new.jsf?faces-redirect=true";
    }

    /**
     * Cancel editing and navigate back to view page
     * 
     * @return outcome "view.jsf?faces-redirect=true&id={id}"
     */
    public String cancelEdit() {
        return "view.jsf?faces-redirect=true&id=" + getEntityId();
    }

    /**
     * Get the lazy model. Inside uses the most common initialization of lazy
     * model {@link AbstractViewBean#createLazyModel(String[])}.
     * 
     * @return lazy model instance
     */
    public BasicLazyDataModel<T> getLazyModel() {
        if (lazyModel == null) {
            createLazyModel(new String[] { "id" });
        }

        return lazyModel;
    }

    /**
     * Set the lazy model for this bean
     * 
     * @param lazyModel
     */
    public void setLazyModel(BasicLazyDataModel<T> lazyModel) {
        this.lazyModel = lazyModel;
    }

    /**
     * Get entity object
     * 
     * @return entity
     */
    public T getEntity() {
        return entity;
    }

    /**
     * Set entity object
     * 
     * @param entity
     */
    public void setEntity(T entity) {
        this.entity = entity;
    }

    /**
     * Used in {@link AbstractViewBean#getLazyModel()}. Override it when you
     * want to do global search on some fields other than "name" field of the
     * entity
     * 
     * @param fields fields of the entity
     */
    protected void createLazyModel(String[] fields) {
        LazyModelProperties props = new LazyModelProperties();
        props.setFields(fields);
        lazyModel = new BasicLazyDataModel<T>(getDAO(), props);
    }

    /**
     * Helper method for picklist processing. Transforms array of String of ids
     * to array of Long
     * 
     * @param array with string ids values
     * 
     * @return list with ids values in Long
     */
    protected List<Long> toIdList(String[] array) {
        List<Long> result = new ArrayList<Long>();
        if (array != null) {
            for (String s : array) {
                result.add(Long.valueOf(s));
            }
        }
        return result;
    }

    protected List<Long> toIdList(BasicModel[] array) {
        List<Long> result = new ArrayList<Long>();
        if (array != null) {
            for (BasicModel s : array) {
                result.add(s.getId());
            }
        }

        return result;
    }

    /**
     * Actions to be performed before save or update
     */
    protected void preSave() {

    }

    protected String handleOptimisticLock(){
        log("Optimistic lock on "+this.entity.getClass()+" entity. Record id: " + getEntityId());
//        this.entity.setVersion(getDAO().find(getEntityId()).getVersion());
        ResourceBundle bundle = ResourceBundle.getBundle("messages");
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("optimistic.lock.message"), "");
        FacesContext.getCurrentInstance().addMessage("errors", fm);
        FacesContext.getCurrentInstance().validationFailed();

        return "";
    }
	
    protected String handleFieldLengthExceeded(){
        ResourceBundle bundle = ResourceBundle.getBundle("messages");
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("max.length.exceed.message"), "");
        FacesContext.getCurrentInstance().addMessage("errors", fm);
        FacesContext.getCurrentInstance().validationFailed();

        return "";
    }

    /**
     * A shortcut for {@link Log#info(Object)}
     * 
     * @param message to log
     */
    protected void log(String message) {
        log.info(message);
    }
}
