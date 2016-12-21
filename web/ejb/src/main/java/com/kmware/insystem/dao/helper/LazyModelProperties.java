package com.kmware.insystem.dao.helper;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LazyModelProperties implements Serializable {
    private static final long serialVersionUID = -3048956650360411413L;

    private int first;
	private int pageSize;
	private String sortField;
	private String sortOrder;
	private Map<String, String> filters;
	private String[] fields;

	public LazyModelProperties() {
		super();
		this.first = 0;
		this.pageSize = 5;
		this.sortField = "";
		this.sortOrder = "asc";
		this.filters = Collections.emptyMap();
		this.fields = new String[0];
	}

	public LazyModelProperties(int first, int pageSize, String sortField,
			String sortOrder, Map<String, String> filters, String[] fields) {
		super();
		this.first = first;
		this.pageSize = pageSize;
		this.sortField = sortField;
		this.sortOrder = sortOrder;
		this.filters = filters;
		this.fields = fields;
	}

	public LazyModelProperties copy() {
		LazyModelProperties dest = new LazyModelProperties();
		dest.setFirst(this.getFirst());
		dest.setPageSize(this.getPageSize());
		dest.setSortField(this.getSortField());
		dest.setSortOrder(this.getSortOrder());
		dest.setFilters(new HashMap<String, String>(this.getFilters()));
		return dest;
	}

	public int getFirst() {
		return first;
	}

	public int getPageSize() {
		return pageSize;
	}

	public String getSortField() {
		return sortField;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public Map<String, String> getFilters() {
		return filters;
	}
	
	public String[] getFields() {
		return fields;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public void setFilters(Map<String, String> filters) {
		this.filters = filters;
	}
	
	public void setFields(String[] fields) {
		this.fields = fields;
	}
	
	public String getGlobalFilterValue(){
		return filters.get("globalFilter");
	}
	
	public String setGlobalFilterValue(String value){
		return filters.put("globalFilter", value);
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(fields);
		result = prime * result + ((filters == null) ? 0 : filters.hashCode());
		result = prime * result + first;
		result = prime * result + pageSize;
		result = prime * result
				+ ((sortField == null) ? 0 : sortField.hashCode());
		result = prime * result
				+ ((sortOrder == null) ? 0 : sortOrder.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LazyModelProperties other = (LazyModelProperties) obj;
		if (!Arrays.equals(fields, other.fields))
			return false;
		if (filters == null) {
			if (other.filters != null)
				return false;
		} else if (!filters.equals(other.filters))
			return false;
		if (first != other.first)
			return false;
		if (pageSize != other.pageSize)
			return false;
		if (sortField == null) {
			if (other.sortField != null)
				return false;
		} else if (!sortField.equals(other.sortField))
			return false;
		if (sortOrder != other.sortOrder)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LazyModelProperties [first=" + first + ", pageSize=" + pageSize
				+ ", sortField=" + sortField + ", sortOrder=" + sortOrder
				+ ", filters=" + filters + ", fields="
				+ Arrays.toString(fields) + "]";
	}
	
	

}
