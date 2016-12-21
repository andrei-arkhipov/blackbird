package com.kmware.insystem.beans.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.kmware.insystem.dao.helper.LazyModelProperties;

@SessionScoped
@Named
public class SessionDataBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5321858527231067950L;
	private Map<String, LazyModelProperties> paginatiorProperties;
	protected Logger log = Logger.getLogger(getClass());
	
	public SessionDataBean(){
		paginatiorProperties = new HashMap<String, LazyModelProperties>();
	}
	
	@PostConstruct
	public void init() {
		
	}
	
	public boolean hasPaginationPropertyFor(String classname){
		return paginatiorProperties.containsKey(classname);
	}
	
	public LazyModelProperties getPaginationPropertiesFor(String classname){
			return paginatiorProperties.get(classname);
	}
	
	public void storePaginatorProperties(String classname, LazyModelProperties properties){
		paginatiorProperties.put(classname, properties);
		log("added properties for class " + classname);
	}
	
	protected void log(String message){
		log.info(message);
	}
}