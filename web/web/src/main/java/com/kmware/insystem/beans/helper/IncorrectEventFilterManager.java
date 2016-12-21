package com.kmware.insystem.beans.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kmware.insystem.beans.reports.BasicReportBean;
import com.kmware.insystem.dao.BasicDAO;
import com.kmware.insystem.model.BasicModel;
import com.kmware.insystem.model.IncorrectEvent;
import com.kmware.insystem.model.User;

/**
 * A most common implementation of extended filter. Extendable if needed
 * 
 * @param <T>
 */
public class IncorrectEventFilterManager<T extends BasicModel> extends BasicReportBean {
    private final static String reportName = "Incorrect EventLog export";
    private final static String templateName_ALL = "com/kmware/insystem/beans/reports/export_wrong_all.jasper";
	
    protected BasicDAO<T> dao;

    protected ExtFilterLazyDataModel<T> model;
    protected Map<String, Object> modelParams;
    protected Date dateFrom;
    protected Date dateTo;
	protected String incorrectEventId;
    protected String global;
    protected String documentType;

	private List<IncorrectEvent> incorrectEvents;
	protected User user;

    public IncorrectEventFilterManager(BasicDAO<T> dao, User user,String type) {
        this.dao = dao;
        this.user = user;
        this.documentType = type.toLowerCase();
        this.modelParams = new HashMap<String, Object>();
        reset();
	}

    /**
     * The search method so to say. The
     * {@code BasicExtendedFilterManager#modelParams} present in lazy model.
     * Here we just fill in valid data to perform search. Further the
     * {@link ExtFilterLazyDataModel#load(int, int, String, org.primefaces.model.SortOrder, Map)}
     * method will be called during ajax partial update.
     */
    public void doFiltering() {
        String searchAll = StringUtils.equals( global+"","")  ? "": "%"+global.toUpperCase()+"%";
        modelParams.clear();
        modelParams.put("incorrectEvent", incorrectEventId);
        modelParams.put("search",searchAll);
        Calendar c = Calendar.getInstance();
        c.set(1970, 0, 1, 0, 0, 0);
        modelParams.put("dateFrom", dateFrom != null ? dateFrom : c.getTime());
        c.set(2200, 0, 1, 0, 0, 0);
        modelParams.put("dateTo", dateTo != null ? dateTo : c.getTime());
        modelParams.put("incorrectEventName", searchAll);
    }

    public void reset() {
        dateFrom = null;
        dateTo = null;
        incorrectEventId = "";
        global="";
        incorrectEvents=null;
        doFiltering();
	}
	
    /**
     * Building a query for specified document. Again only the common fields for
     * all documents are used. If extending this class you can add up some extra
     * predicates into string builder starting with AND / OR and there it goes
     * 
     * @return a string builder that holds the query
     */
    protected StringBuilder generateQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append(" FROM ") 
        .append(dao.getParameterClass(0).getName())
        .append(" d WHERE (:incorrectEvent = '' OR d.incorrectEvent.id = :incorrectEvent) ")
        .append("AND (d.createDate BETWEEN :dateFrom  AND :dateTo ) ")
        .append("AND (  ((:incorrectEvent ='' AND :incorrectEventName!='' AND UPPER(d.incorrectEvent.name) LIKE :incorrectEventName) ")
        .append("     OR (:incorrectEvent ='' AND :incorrectEventName!='' AND UPPER(d.incorrectEvent.name) LIKE :incorrectEventName)) ")
        .append("     OR (:search='' OR d.searchField LIKE :search)   ) ");
        return sb;
    }

    public Map<String,Object> saveState(){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("dateFrom", dateFrom);
        map.put("dateTo", dateTo);
        map.put("incorrectEvent", incorrectEventId);
        map.put("global",global);
        return map;
    }
	
    public void restoreState(Map<String,Object> map){
        dateFrom = (Date) map.get("dateFrom");
        dateTo = (Date) map.get("dateTo");
        incorrectEventId = (String) map.get("incorrectEvent");
        global = (String) map.get("global");
    }
	
	public void incorrectEventSelected(){
		incorrectEvents = null;
		incorrectEventId = "";
    }

    /**
     * Get the datamodel for document. During instantiation it's prefilled with
     * the builded query and initial model parameters, that are further changed
     * in doFilter method
     * 
     * @return a valid lazy data model instance
     */
    public ExtFilterLazyDataModel<T> getModel() {
        if (model == null) {
            model = new ExtFilterLazyDataModel<T>(dao);
            String queryBody = generateQuery().toString();
            model.setSelectQuery(queryBody);
            model.setParameters(modelParams);
        }
        return model;
    }

	public void setModel(ExtFilterLazyDataModel<T> model) {
		this.model = model;
	}

    @SuppressWarnings("unchecked")
    public List<IncorrectEvent> getIncorrectEvents() {
        if (incorrectEvents == null) {
        	incorrectEvents = (List<IncorrectEvent>)dao.getResultList("FROM " + IncorrectEvent.class.getName() + " s ORDER BY s.name ASC",null);
        }
        return incorrectEvents;
    }

    public void setIncorrectEvents(List<IncorrectEvent> incorrectEvents) {
        this.incorrectEvents = incorrectEvents;
    }

    public Date getdateFrom() {
        return dateFrom;
    }

    public void setdateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getdateTo() {
        return dateTo;
    }

    public void setdateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getIncorrectEventId() {
        return incorrectEventId;
    }

    public void setIncorrectEventId(String incorrectEventId) {
        this.incorrectEventId = incorrectEventId;
    }

    public String getGlobal() {
        return global;
    }

    public void setGlobal(String global) {
        this.global = global;
    }


	@Override
	public String getTemplateFileName() {
        return templateName_ALL;
    }

    @Override
    public String getReportFileName() {
        return reportName;
    }

    @Override
    protected Map<String, Object> processParams() {
        Map<String, Object> result = new HashMap<String, Object>();
        String searchAll = StringUtils.equals( global+"","")  ? "": "%"+global.toUpperCase()+"%";
        result.put("incorrectEvent_id", incorrectEventId);
        result.put("search_field",searchAll);
        Calendar c = Calendar.getInstance();
        c.set(1970, 0, 1, 0, 0, 0);
        result.put("create_date_start", dateFrom != null ? dateFrom : c.getTime());
        c.set(2200, 0, 1, 0, 0, 0);
        result.put("create_date_end", dateTo != null ? dateTo : c.getTime());
        result.put("incorrectEventName", searchAll);
        return result;
    }

}