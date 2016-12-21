package com.kmware.insystem.beans.helper;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.kmware.insystem.dao.BasicDAO;
import com.kmware.insystem.model.BasicModel;

/**
 * Extended version of lazy data model used for event log
 *
 * @param <T>
 */
public class ExtFilterLazyDataModel<T extends BasicModel> extends LazyDataModel<T> {
    private static final long serialVersionUID = 1923697700181707402L;

    protected List<T> wrappedData;
    protected BasicDAO<T> dao;
    protected Map<String, Object> parameters;
    protected String selectQuery = "";
    protected int first = 0;

    public ExtFilterLazyDataModel(BasicDAO<T> dao) {
        this.dao = dao;
    }

    /**
     * A method executed by p:dataTable on any table updates like page change etc.
     */
    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
        String sort = null;
        if (sortField != null) {
            switch (sortOrder) {
                case ASCENDING:
                    sort = " " + sortField + " ASC ";
                    break;
                case DESCENDING:
                    sort = " " + sortField + " DESC ";
                    break;
                default:
                    break;
            }
        } else {
            sortField = "createDate";
            sort = sortField + " DESC ";
        }

        List<T> result = dao.getResultList(selectQuery, parameters, sort, pageSize, first);
        this.setRowCount(dao.getResultCount(selectQuery, parameters).intValue());
        this.setPageSize(pageSize > 0 ? pageSize : 10);
        this.setWrappedData(result);
        return result;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    /**
     * Our linking of the lazy data model with {@link BasicExtendedFilterManager}
     * this is how we get out values from filter form into query
     * 
     * @param parameters
     */
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public String getSelectQuery() {
        return selectQuery;
    }
	
    public void setSelectQuery(String selectQuery) {
        this.selectQuery = selectQuery;
    }

}
