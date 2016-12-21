package com.kmware.insystem.dao;

import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;

import com.kmware.insystem.model.Permission;
import com.kmware.insystem.model.Role;
import com.kmware.insystem.model.RolePermission;

@Stateless
public class RoleDAO extends BasicDAO<Role>{
    private static final long serialVersionUID = -694645541934957257L;

    @SuppressWarnings("unchecked")
	public List<Role> getRoleList(){
        return em.createQuery("SELECT r FROM Role r WHERE r.name != 'jboss'").getResultList();
    }

    public Role getRoleByName(String name){
        return (Role) em.createQuery("SELECT r FROM Role r WHERE r.name = :name")
                .setParameter("name", name).getSingleResult();
    }

    public Role getRoleByInternalName(String name){
        return (Role) em.createQuery("SELECT r FROM Role r WHERE r.internalName = :name")
                .setParameter("name", name).getSingleResult();
    }

    public Boolean isRoleNameExist(String name) {
        Long result =(Long)em.createQuery("SELECT COUNT(*) FROM Role role WHERE role.name=:name")
                .setParameter("name", name).getSingleResult();	
        return (result !=0) ? true:false;
    }

    public Boolean isRoleInternalNameExist(String name) {
        Long result =(Long)em.createQuery("SELECT COUNT(*) FROM Role role WHERE role.internalName=:name")
                .setParameter("name", name).getSingleResult();	
        return (result !=0) ? true:false;
    }

    @SuppressWarnings("unchecked")
	public List<String> getReadPermissions(Long id) {
        return (List<String>) em.createNativeQuery("SELECT perm.internal_name FROM roles_permissions rp INNER JOIN permissions perm ON rp.permission_id=perm.id INNER JOIN roles role ON role.id=rp.role_id WHERE role.id= :id AND rp.canRead=1")
                .setParameter("id", id).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<String> getChangePermissions(Long id) {
        return (List<String>) em.createNativeQuery("SELECT perm.internal_name FROM roles_permissions rp INNER JOIN permissions perm ON rp.permission_id=perm.id INNER JOIN roles role ON role.id=rp.role_id WHERE role.id= :id AND rp.canChange=1")
                .setParameter("id", id).getResultList();
    }

    @SuppressWarnings("unchecked")
	public List<Permission> getPermissionsList(){
        return em.createQuery("SELECT DISTINCT p FROM Permission p join fetch p.permissions ORDER BY p.orderNumber").getResultList();
    }

    @SuppressWarnings("unchecked")
	public List<Permission> getPermissionsList2(Object object){
        return em.createQuery("SELECT DISTINCT p FROM Permission p join fetch p.permissions WHERE p.internalName NOT IN (SELECT rp.permission.internalName FROM RolePermission rp WHERE rp.role.id = :roleid) ORDER BY p.orderNumber").setParameter("roleid", object).getResultList();
    }

    @SuppressWarnings("unchecked")
	public List<Permission> findWhereIdInForPerm(Object array){
        return em.createQuery("SELECT t FROM Permission t WHERE t.id in (:list)").setParameter("list", array).getResultList();
    }

    @SuppressWarnings("unchecked")
	public List<Permission> findPermissionsByRoleId(Long id){
        List<String> ids = em.createNativeQuery("SELECT p.id FROM permissions p WHERE p.id IN " +
                "(SELECT rp.permissions_id FROM roles_permissions rp WHERE rp.roles_id = :id)")
                .setParameter("id", id)
                .getResultList();
        if(!(ids.size()>0)){
            return Collections.emptyList();
        }
        return em.createQuery("SELECT p FROM Permission p WHERE p.id IN :ids").setParameter("ids", ids).getResultList();
    }

    public void saveRP(RolePermission rp){
        em.persist(rp);
    }

    public void removeRP(RolePermission rp){
        long id = rp.getId();
        em.createNativeQuery("DELETE FROM roles_permissions p WHERE p.id = :id").setParameter("id", id).executeUpdate();
    }

	public List<Permission> getPermissionsList2(Long entityId) {
		// TODO Auto-generated method stub
		return null;
	}
}
