package com.kmware.insystem.beans.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.kmware.insystem.dao.BasicDAO;
import com.kmware.insystem.dao.helper.LazyModelProperties;
import com.kmware.insystem.model.BasicModel;

public class BasicLazyDataModel<T extends BasicModel> extends LazyDataModel<T> {
    private static final long serialVersionUID = 8952318579835482795L;

    protected LazyModelProperties criteria;

    protected LazyModelProperties oldCriteria;

    protected List<T> wrappedData;

    protected BasicDAO<T> jpaService;

    protected T selected;

    public BasicLazyDataModel(BasicDAO<T> jpaService, LazyModelProperties criteria) {
        this.jpaService = jpaService;
        this.criteria = criteria;
        this.oldCriteria = new LazyModelProperties();
        this.selected = null;
        this.wrappedData = new ArrayList<T>();
    }

    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
        this.criteria.setFirst(first);
        this.criteria.setPageSize(pageSize);
        setPageSize(pageSize);
        this.criteria.setSortField(sortField);
        this.criteria.setSortOrder(sortOrder == SortOrder.ASCENDING? "ascending" : "descending");
        this.criteria.setFilters(filters);
		
        List<T> data = new ArrayList<T>();
        data = jpaService.lazyLoad(criteria);
        setRowCount(jpaService.lazyLoadCount(criteria));
        setWrappedData(data);
        return data;
    }

    @Override
    public int getRowCount() {
        return super.getRowCount();
    }

    public T getSelected() {
        return selected;
    }

    public void setSelected(T selected) {
        this.selected = selected;
    }

    public LazyModelProperties getCriteria() {
        return criteria;
    }

    protected Class<?> getParameterClass(int pos) {
        return jpaService.getParameterClass(pos);
    }
    @Override  
    public Object getRowKey(T entity) {  
        return entity.getId();  
    }

	@Override
	public T getRowData(String rowKey) {
		int id = Integer.parseInt(rowKey);
		return wrappedData.get(id);
	}

	@Override
	public Object getWrappedData() {
		return this.wrappedData;
	}

	public void setWrappedData(List<T> list) {
		this.wrappedData = list;
	}

}
