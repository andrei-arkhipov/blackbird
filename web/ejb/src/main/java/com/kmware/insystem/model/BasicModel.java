package com.kmware.insystem.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class BasicModel implements Serializable {

	private static final long serialVersionUID = 8451194292683875385L;
	protected long id;
//	protected Long version;
//	protected Boolean deleted;
	
	public BasicModel(){
		this.id = -1;
//		this.deleted = false;
//		this.version = 0L;
	}
	
	public abstract void loadLazyCollections();

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

//	@Version
//	public Long getVersion() {
//		return version;
//	}

//	public void setVersion(Long version) {
//		this.version = version;
//	}
	
//	@Column(name="deleted")
//	public Boolean getDeleted() {
//		return deleted;
//	}

//	public void setDeleted(Boolean deleted) {
//		this.deleted = deleted;
//	}

	@Override
	public String toString() {
        return "BasicModel [id=" + id +  "]";
	}

	
	
	
}