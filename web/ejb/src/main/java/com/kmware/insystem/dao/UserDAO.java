package com.kmware.insystem.dao;

import javax.ejb.Stateless;

import com.kmware.insystem.model.User;

@Stateless
public class UserDAO extends BasicDAO<User> {
    private static final long serialVersionUID = -3525273885778896614L;

    public User findByUserName(String username, boolean loadLazyCollections){
        User result =(User)em.createQuery("SELECT user FROM User user WHERE user.userName=:username")
                .setParameter("username", username).getSingleResult();	
        if (loadLazyCollections && result != null) {
            result = loadLazyCollections(result);
        }
        return result;
    }

    public User findUserById(Integer id, boolean loadLazyCollections){

    	User result =(User)em.createQuery("SELECT u FROM User u WHERE u.id=:id").setParameter("id", id).getSingleResult();
        if(loadLazyCollections && result!= null){
        	result = loadLazyCollections(result);
        }
        return result;
    }

    public Boolean isUserExist(String username) {
    	Long result =(Long)em.createQuery("SELECT COUNT(*) FROM User user WHERE user.userName=:username")
                .setParameter("username", username).getSingleResult();	
    	return (result !=0) ? true:false;
    }
}
