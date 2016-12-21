package com.kmware.insystem.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class BasicFilteredModel extends BasicModel {
    private static final long serialVersionUID = -4249956718824242107L;

    protected Date createDate;
    protected String searchField;

    public BasicFilteredModel() {
        this.createDate = new Date();
        this.searchField = "";
    }

    @PrePersist
    public void prePersist() {
        this.createDate = new Date();
    }

    @PreUpdate
    public void preUpdate() {

    }
	
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "event_date", nullable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "search_field")
    public String getSearchField() {		
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public abstract String documentType();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
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
        BasicFilteredModel other = (BasicFilteredModel) obj;
        if (createDate == null) {
            if (other.createDate != null)
                return false;
        } else if (!createDate.equals(other.createDate))
            return false;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "BasicFilteredModel [id=" + id + ", createDate=" + createDate + "]";
    }
}
