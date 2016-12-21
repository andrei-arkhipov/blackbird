package com.kmware.insystem.dao;

import javax.ejb.Stateless;

import com.kmware.insystem.model.Device;

import java.util.List;

@Stateless
public class DeviceDAO  extends BasicDAO<Device>{
    private static final long serialVersionUID = -6656046745760788312L;

    @SuppressWarnings("unchecked")
	public List<Device> getAllDeviceNumbers(){
        return (List<Device>) em.createQuery("select t from Device t").getResultList();
    }

    public Boolean isDeviceNumberExist(String deviceNumber) {
    	Long result =(Long)em.createQuery("SELECT COUNT(*) FROM Device t WHERE t.deviceNumber=:deviceNumber")
                .setParameter("deviceNumber", deviceNumber).getSingleResult();	
    	return (result !=0) ? true:false;
    }
}
