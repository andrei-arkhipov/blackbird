package com.kmware.insystem.beans.view;

import java.awt.Color;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.kmware.insystem.beans.helper.BasicLazyDataModel;
import com.kmware.insystem.beans.helper.IncorrectEventFilterManager;
import com.kmware.insystem.beans.helper.IncorrectEventLogFilter;
import com.kmware.insystem.beans.session.UserSessionBean;
import com.kmware.insystem.dao.IncorrectEventDAO;
import com.kmware.insystem.model.IncorrectEventLog;

/**
 * Extension of AbstractFilteredBean for EventLog' specific operation mechanisms
 * 
 * @param <T>
 */
public abstract class AbstractIncorrectEventLogBean<T extends IncorrectEventLog> extends AbstractViewBean<T> {
    private static final long serialVersionUID = 2433346707544742736L;

    protected static final String SEQUENCE_QUERY = "sequence.query";

    protected ResourceBundle environment = ResourceBundle.getBundle("env");
    protected ResourceBundle docmsgs = ResourceBundle.getBundle("eventlog_messages");
	protected Integer first;
	protected Integer perPage;

	/**
	 * Needed for document filtering
	 */
	@Inject
	protected UserSessionBean userSession;

    @Inject
    protected IncorrectEventDAO incorrectEventDAO;


   /**
    * The filter that is used during filling the form of the document
    */
    protected IncorrectEventLogFilter filter;

    @Override
    public void init() {
        filter = new IncorrectEventLogFilter(incorrectEventDAO);
        first = 0;
        perPage = 10;
    }

    @Override
    public Object getEntityId() {
        return entity.getId();
    }

    protected String[] getFields() {
        return new String[] { "searchField" };
    }

    @Override
    public BasicLazyDataModel<T> getLazyModel() {
        if (lazyModel == null) {
            createLazyModel(getFields());
        }
        return lazyModel;
    }

	@Override
	public void processUrlParameters() {
		if (!FacesContext.getCurrentInstance().isPostback()) {
			super.processUrlParameters();
			filter.setEntity(entity);
            this.getFilterManager().doFiltering();
		}
	}

    /**
     * Getter for common info filter
     * 
     * @return valid instance
     */
    public IncorrectEventLogFilter getFilter() {
        return filter;
    }

    /**
     * Setter for common info filter
     * 
     */
    public void setFilter(IncorrectEventLogFilter filter) {
        this.filter = filter;
    }

    /**
     * Abstract method. Every document has it's specific extended filter.
     * {@link BasicExtendedFilterManager} is the most common implementation. Can
     * be easily extended
     * 
     * @return a valid instance
     */
    public abstract IncorrectEventFilterManager<T> getFilterManager();

    /**
     * A setter for extended filter.
     * 
     * @see AbstractIncorrectEventLogBean#getFilterManager()
     * @param extendedFilterManager
     */
    public abstract void setFilterManager(IncorrectEventFilterManager<T> filterManager);

    protected boolean validationOk() {
        return true;
    }

    public void saveState() {
//        log("Saving bean state for " + entity.documentType());
        Map<String, Object> map = getFilterManager().saveState();
        map.put("first", first);
        map.put("perPage", perPage);
//        this.userSession.setValueFor(entity.documentType(), map);
//        log("saving done");
    }

    @SuppressWarnings("unchecked")
    public void restoreState() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
//            log("Restoring bean state for " + entity.documentType());
//            Map<String, Object> map = (Map<String, Object>) this.userSession.getValueFor(entity.documentType());
//            if (map == null) {
//                log("Restoring abort");
                return;
//            }
//            first = (Integer) map.get("first");
//            perPage = (Integer) map.get("perPage");
//            getFilterManager().restoreState(map);
//            getFilterManager().doFiltering();
//            log("restoring done");
        }
    }

    public void doFiltering() {
        getFilterManager().doFiltering();
        saveState();
    }

    public void resetFilter() {
        getFilterManager().reset();
        doFiltering();
    }

    public String getSuitableColor(String color) {
        if (StringUtils.isBlank(color)) {
            color = "ffffff";
        }
        // convert hex string to int
        int rgb = Integer.parseInt(color, 16);

        Color c = new Color(rgb);

        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);

        float brightness = hsb[2];

        if (brightness < 0.5) {
            return "ffffff";
        } else {
            return "000000";
        }
    }

    public Integer getFirst() {
        return first;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

}