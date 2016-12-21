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
import com.kmware.insystem.model.CardHolder;
import com.kmware.insystem.model.Device;
import com.kmware.insystem.model.Direction;
import com.kmware.insystem.model.Organization;
import com.kmware.insystem.model.User;

/**
 * A most common implementation of extended filter. Extendable if needed
 * 
 * @param <T>
 */
public class BasicExtendedFilterManager<T extends BasicModel> extends BasicReportBean {
    private final static String reportName = "EventLog export";
    private final static String templateName_ALL = "com/kmware/insystem/beans/reports/export_rdoc_all.jasper";
	
    protected BasicDAO<T> dao;

    protected ExtFilterLazyDataModel<T> model;
    protected Map<String, Object> modelParams;
    protected Date dateFrom;
    protected Date dateTo;
    protected String organizationId;
	protected String deviceId;
	protected String directionId;
	protected String cardHolderId;
    protected String global;
    protected String documentType;

    private List<Organization> organizations;
	private List<Device> devices;
	private List<Direction> directions;
	private List<CardHolder> cardHolders;
	protected User user;

    public BasicExtendedFilterManager(BasicDAO<T> dao, User user,String type) {
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
        modelParams.put("organization", organizationId);
        modelParams.put("device", deviceId);
        modelParams.put("direction", directionId);
        modelParams.put("cardHolder", cardHolderId);
        modelParams.put("search",searchAll);
        Calendar c = Calendar.getInstance();
        c.set(1970, 0, 1, 0, 0, 0);
        modelParams.put("dateFrom", dateFrom != null ? dateFrom : c.getTime());
        c.set(2200, 0, 1, 0, 0, 0);
        modelParams.put("dateTo", dateTo != null ? dateTo : c.getTime());
        modelParams.put("organizationName", searchAll);
        modelParams.put("directionName", searchAll);
    }

    public void reset() {
        dateFrom = null;
        dateTo = null;
        organizationId = "";
        deviceId = "";
        directionId = "";
        cardHolderId = "";
        global="";
        organizations=null;
        devices = null;
        directions = null;
        cardHolders = null;
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
        .append(" d WHERE (:organization = '' OR d.device.organization.id = :organization) ")
        .append("AND (:device = '' OR d.device.id = :device) ")
        .append("AND (:direction = '' OR d.direction.id = :direction) ")
        .append("AND (d.createDate BETWEEN :dateFrom  AND :dateTo ) ")
        .append("AND (:cardHolder ='' OR d.cardHolder.id = :cardHolder ) ")
        .append("AND (  ((:direction='' AND :directionName!='' AND UPPER(d.direction.name) LIKE :directionName) ")
        .append("     OR (:organization ='' AND :organizationName!='' AND UPPER(d.device.organization.name) LIKE :organizationName)) ")
        .append("     OR (:search='' OR d.searchField LIKE :search)   ) ");
        return sb;
    }

    public Map<String,Object> saveState(){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("dateFrom", dateFrom);
        map.put("dateTo", dateTo);
        map.put("organization", organizationId);
        map.put("device", deviceId);
        map.put("direction", directionId);
        map.put("cardHolder", cardHolderId);
        map.put("global",global);
        return map;
    }
	
    public void restoreState(Map<String,Object> map){
        dateFrom = (Date) map.get("dateFrom");
        dateTo = (Date) map.get("dateTo");
        organizationId = (String) map.get("organization");
        deviceId = (String) map.get("device");
        directionId = (String) map.get("direction");
        cardHolderId = (String) map.get("cardHolder");
        global = (String) map.get("global");
    }
	
	public void organizationSelected(){
		devices = null;
		deviceId = "";
		directions = null;
		directionId = "";
    }

    public void deviceSelected(){
		directions = null;
		directionId = "";
    }
	
    public void directionSelected(){

    }

    public void cardHolderSelected(){

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
    public List<Organization> getOrganizations() {
        if (organizations == null) {
        	organizations = (List<Organization>)dao.getResultList("FROM " + Organization.class.getName() + " s ORDER BY s.name ASC",null);
        }
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    @SuppressWarnings("unchecked")
    public List<Device> getDevices() {
        if (devices == null ) {
            if(StringUtils.isBlank(organizationId) ){
                devices = (List<Device>) dao.getResultList("FROM " + Device.class.getName() + " d ORDER BY d.deviceNumber ASC", null);
            } else {
                devices = (List<Device>) dao.getResultList("FROM " + Device.class.getName() + " d WHERE d.organization.id='" + Long.parseLong(organizationId) + "' ORDER BY d.deviceNumber ASC", null);
            }
        }
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    @SuppressWarnings("unchecked")
    public List<Direction> getDirections() {
        if (directions == null) {
            directions = (List<Direction>)dao.getResultList("FROM " + Direction.class.getName() + " d  WHERE d.id != 3 ORDER BY d.name ASC",null);
        }
        return directions;
    }

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    @SuppressWarnings("unchecked")
    public List<CardHolder> getCardHolders() {
        if (cardHolders == null) {
        	cardHolders = (List<CardHolder>)dao.getResultList("FROM " + CardHolder.class.getName() + " c ORDER BY c.cardNumber ASC",null);
        }
        return cardHolders;
    }

    public void setCardHolders(List<CardHolder> cardHolders) {
        this.cardHolders = cardHolders;
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

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDirectionId() {
		return directionId;
	}

	public void setDirectionId(String directionId) {
		this.directionId = directionId;
	}

	public String getCardHolderId() {
		return cardHolderId;
	}

	public void setCardHolderId(String cardHolderId) {
		this.cardHolderId = cardHolderId;
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
        result.put("organization_id", organizationId);
        result.put("device_id", deviceId);
        result.put("direction_id", directionId);
        result.put("cardHolder_id", cardHolderId);
        result.put("search_field",searchAll);
        Calendar c = Calendar.getInstance();
        c.set(1970, 0, 1, 0, 0, 0);
        result.put("create_date_start", dateFrom != null ? dateFrom : c.getTime());
        c.set(2200, 0, 1, 0, 0, 0);
        result.put("create_date_end", dateTo != null ? dateTo : c.getTime());
        result.put("organization_name", searchAll);
        result.put("direction_name", searchAll);
        return result;
    }

}