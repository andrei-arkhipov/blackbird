package com.kmware.insystem.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.kmware.insystem.dao.helper.LazyModelProperties;
import com.kmware.insystem.model.BasicModel;

/**
 * Common DAO for all entities that provides methods for database manipulations
 * 
 * 
 * @param <T>  entity class Parameter
 */
public class BasicDAO<T extends BasicModel> implements Serializable {
    private static final long serialVersionUID = -5461773209686171295L;

    /**
     * The entity manager that is handled automatically
      */
    @PersistenceContext
    protected EntityManager em;

    /**
     * Calls {@link BasicDAO#find(Object, boolean)} with boolean parameter set
     * to false
     */
    public T find(Object id) {
        return find(id, false);
    }

    public Object getSingleResult(String query,Map<String, Object> params) {
        Query q = getResultQuery(query, params, null);
        return q.getSingleResult();
    }

    public List<?> getResultList(Class<?> clazz){
        return em.createQuery("SELECT e FROM " + clazz.getName()+" e").getResultList();
    }

    /**
     * Find entity with given id and load all it's lazy collections
     * 
     * @param id
     *            of the entity to load
     * @param loadLazyCollections
     *            if true - load lazy collections, else do nothing
     * @return entity object or null if nothing is found
     */
    @SuppressWarnings("unchecked")
    public T find(Object id, boolean loadLazyCollections) {
        T result = (T) em.find(getParameterClass(0), id);
        if (loadLazyCollections && result != null) {
            result = loadLazyCollections(result);
        }
        return result;
    }

    public <E extends BasicModel> E find(Object id,boolean loadLazyCollections, Class<E> clazz){
        E result = (E) em.find(clazz, id);
        if (loadLazyCollections && result != null) {
            result.loadLazyCollections();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<T> findWhereIdIn(Object array) {
        return em.createQuery("SELECT t FROM " + getParameterClass(0).getName() + " t WHERE t.id in (:list)").setParameter("list", array).getResultList();
    }

    /**
     * Used to load lazy collections in {@link BasicDAO#find(Object, boolean)}
     * 
     * @param entity
     *            the entity from db
     * @return entity with loaded lazy collections
     */
    protected T loadLazyCollections(T entity) {
        entity.loadLazyCollections();
        return entity;
    }

    /**
     * Get the resulting list of all entities in databse
     * 
     * @return list of entities if found
     * @see BasicDAO#getResultList(String, Map)
     * @see BasicDAO#getResultList(String, Map, String, int, int)
     */
    public List<T> getResultList() {
        return this.getResultList(null, new HashMap<String, Object>());
    }

    /**
     * Get the resulting list of entities that match parameters
     * 
     * @param ejbQL
     *            query to database. Can be null
     * @param parameters
     *            map of K V parameters used in query
     * @return list of entities if found
     * @see BasicDAO#getResultList()
     * @see BasicDAO#getResultList(String, Map, String, int, int)
     */
    public List<T> getResultList(String ejbQL, Map<String, Object> parameters) {
        return getResultList(ejbQL, parameters, null, 0, 0);
    }

    /**
     * Get the limited resulting list of entities that match parameters
     * 
     * @param ejbQL
     *            query to database. Can be null
     * @param parameters
     *            map of K V parameters used in query
     * @param orderBy
     *            defines order of the collection
     * @param maxResults
     *            the limit of the resulting collection
     * @param offset
     *            the first element from the resulting select query to start
     *            with
     * @return list of entities if found
     * @see BasicDAO#getResultList()
     * @see BasicDAO#getResultList(String, Map)
     */
    @SuppressWarnings("unchecked")
    public List<T> getResultList(String ejbQL, Map<String, Object> parameters,
        String orderBy, int maxResults, int offset) {
        Query query = getResultQuery(ejbQL, parameters, orderBy);
        if (maxResults > 0)
            query.setMaxResults(maxResults);
        if (offset > 0)
            query.setFirstResult(offset);
        return query.getResultList();
    }

    /**
     * Get the number of entities that match parameters
     * 
     * @param ejbQL
     *            query to database. Can be null
     * @param parameters
     *            map of K V parameters used in query
     * @return entity count
     * @see BasicDAO#getResultList()
     * @see BasicDAO#getResultList(String, Map)
     * @see BasicDAO#getResultList(String, Map, String, int, int)
     */
    public Long getResultCount(String ejbQL, Map<String, Object> parameters) {
        Query query = getResultQuery("SELECT COUNT(*) " + ejbQL, parameters, null);
        return (Long) query.getSingleResult();
    }

    /**
     * Used to create query with parameters given
     * 
     * @param ejbQL
     *            query to database. Can be null
     * @param parameters
     *            map of K V parameters used in query
     * @return query instance
     */
    @SuppressWarnings("unchecked")
    protected Query getResultQuery(String ejbQL, Map<String, Object> parameters, String orderBy) {
        if (ejbQL == null) {
            Class<T> entityClass = (Class<T>) getParameterClass(0);
            ejbQL = "FROM " + entityClass.getName();
        }
        if (orderBy != null) {
            ejbQL += " ORDER BY " + orderBy;
        }
        Query query = em.createQuery(ejbQL);
        if (parameters != null) {
            for (String paramName : parameters.keySet()) {
                query.setParameter(paramName, parameters.get(paramName));
            }
        }
        return query;
    }

    /**
     * Method is used in
     * {@link LazyDataModel#load(int, int, String, SortOrder, Map)} method to
     * get list of entities
     * 
     * @param props
     *            holds the parameters for querying database
     * @return entity list
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<T> lazyLoad(LazyModelProperties props) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery query = criteriaBuilder.createQuery(getParameterClass(0));
        Root from = query.from(getParameterClass(0));

        if (props.getSortField() != null) {
            if (props.getSortOrder() != null) {
                if (props.getSortOrder() == "ascending") {
                    query.orderBy(criteriaBuilder.asc(getPath(from, null, props.getSortField())));
                } else {
                    query.orderBy(criteriaBuilder.desc(getPath(from, null, props.getSortField())));
                }
            }
        }

        // filters
        Predicate[] predicates = getPredicates(criteriaBuilder, from, props);

        if (predicates.length > 0) {
            Predicate p = criteriaBuilder.or(predicates);
            Predicate[] extraPredicates = extraFiltering();
            if(extraPredicates.length > 0){
                Predicate pExtra = criteriaBuilder.and(extraPredicates);
                p = criteriaBuilder.and(new Predicate[]{p,pExtra});
            }
            query.where(p);
        }

        return em.createQuery(query).setFirstResult(props.getFirst()).setMaxResults(props.getPageSize()).getResultList();
    }
	
    public Predicate[] extraFiltering(){
        return new Predicate[0];
    }

    /**
     * Similar to {@link BasicDAO#lazyLoad(LazyModelProperties)} but returns the
     * number of records
     * 
     * @param props
     *            holds the parameters for querying database
     * @return number of records
     */
    @SuppressWarnings("rawtypes")
    public int lazyLoadCount(LazyModelProperties props) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root from = query.from(getParameterClass(0));
        query.select(criteriaBuilder.count(from));

        // filters
        Predicate[] predicates = getPredicates(criteriaBuilder, from, props);
        if (predicates.length > 0) {
            query.where(criteriaBuilder.or(predicates));
        }
        return em.createQuery(query).getSingleResult().intValue();
    }

    /**
     * Forms predicates for where clause in
     * {@link BasicDAO#lazyLoad(LazyModelProperties)} and
     * {@link BasicDAO#lazyLoadCount(LazyModelProperties)} queries
     * 
     * @param criteriaBuilder
     *            previously calculated in linked methods
     * @param from
     *            Root previously calculated in linked methods
     * @param props
     *            holds the parameters for querying database
     * @return array of predicates
     */
    @SuppressWarnings({ "rawtypes" })
    protected Predicate[] getPredicates(CriteriaBuilder criteriaBuilder, Root from, LazyModelProperties props) {
        List<Predicate> predicates = new ArrayList<Predicate>();

        String globalFilter = props.getFilters().get("globalFilter");
        if (StringUtils.isBlank(globalFilter)) {
            for (Iterator<String> it = props.getFilters().keySet().iterator(); it.hasNext();) {
                String filterProperty = it.next(); // table column name =
                if (StringUtils.equals("globalFilter", filterProperty)) {
                    continue;
                }
                // field name
                String filterValue = props.getFilters().get(filterProperty);

                Expression<String> literal = criteriaBuilder.literal("%" + (String) filterValue.toLowerCase() + "%");
                // TODO try to add full path resolve
                Expression<String> field = criteriaBuilder.lower(getPath(from, null, filterProperty));
                predicates.add(criteriaBuilder.like(field, literal));
            }
        } else {
            globalFilter = globalFilter.toLowerCase();
            for (String fieldName : props.getFields()) {
                Expression<String> literal = criteriaBuilder.literal("%" + (String) globalFilter + "%");
                Expression<String> field = criteriaBuilder.lower(getPath(from, null, fieldName));
                predicates.add(criteriaBuilder.like(field, literal));
            }
        }
        return predicates.toArray(new Predicate[0]);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Path<String> getPath(Root from, Path path, String fieldName) {
        if (StringUtils.contains(fieldName, ".") && from != null) {
            int index = fieldName.indexOf(".");
            return getPath(null, from.get(fieldName.substring(0, index)), fieldName.substring(index + 1));
        }

        if (StringUtils.contains(fieldName, ".") && from == null && path != null) {
            int index = fieldName.indexOf(".");
            return getPath(null, path.get(fieldName.substring(0, index)), fieldName.substring(index + 1));
        }

        if (!StringUtils.contains(fieldName, ".") && from == null && path != null) {
            return path.get(fieldName);
        }
        return from.get(fieldName);
    }

    /**
     * Saves new entity in database
     * 
     * @param entity to save
     */
    public void save(T entity) {
        em.persist(entity);
    }

    /**
     * Updates existing entity in database
     * 
     * @param entity to update
     * 
     * @return saved entity instance
     */
    public T update(T entity) {
        return em.merge(entity);
    }

    @SuppressWarnings("rawtypes")
    public List getNativeQueryResult(String query){
        return em.createNativeQuery(query).getResultList();
    }

    /**
     * Removes entity in database
     * 
     * @param entity to delete
     */
    public void delete(T entity) {
        em.remove(entity);
    }

    /**
     * Returns the entity manager retrieved with PersistenceContext
     * 
     * @return entity manager instance
     */
    public EntityManager getEntityManager() {
        return em;
    }

    /**
     * Get the class from parameter for current DAO implementation
     * 
     * @param pos
     *            the position of parameter. For now you should always pass 0
     *
     * @return the class from parameter
     */
    public Class<?> getParameterClass(int pos) {
        Class<?> entityClass = null;
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            // If called from EntityHomeI18nBean
            if (pType.getActualTypeArguments().length > 0) {
                entityClass = (Class<?>) pType.getActualTypeArguments()[pos];
            }
        }
        if (entityClass == null) {
            throw new IllegalArgumentException("Can not get entity class from generic parameters");
        }
        return entityClass;
    }

    public List<T> getResultActiveList(){
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("active", false);
        String ejbQL  = "SELECT t FROM "+getParameterClass(0).getName()+" t WHERE t.deleted =:active";
        return getResultList(ejbQL, parameters);
    }
	
}
