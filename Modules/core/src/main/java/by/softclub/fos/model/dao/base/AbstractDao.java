package by.softclub.fos.model.dao.base;

import org.apache.log4j.Logger;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.jpa.JpaHelper;
import org.eclipse.persistence.queries.QueryByExamplePolicy;
import org.eclipse.persistence.queries.ReadObjectQuery;
import org.eclipse.persistence.sessions.CopyGroup;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @param <T>  - Entity Type
 * @param <ID> - Primary Key Type
 */
public abstract class AbstractDao<T extends BaseEntity<ID>, ID extends Serializable> implements IDao<T, ID> {
    private static final long serialVersionUID = -3029262935995337570L;

    protected Class<T> type;

    @PersistenceContext(unitName = "NewModelIP")
    protected EntityManager em;

    protected static final Logger DEBUG_LOGGER = Logger.getLogger("DEBUG_LOGGER");

    protected AbstractDao(Class<T> type) {
        this.type = type;
    }


    /**
     * get() always returns something. Null if a entity doesn't exist
     *
     * @param id identifier
     */
    public T find(ID id) {
        final T entity = em.find(type, id);
        if (entity != null) {
            em.refresh(entity);
        }
        return entity;
    }

    public List<T> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(type);
        List<T> result = em.createQuery(query).getResultList();
        return result;
    }


    /**
     * @param first    begin index
     * @param pageSize count per find
     * @return list of items
     */
    public List<T> readPage(int first, int pageSize, String sortField, Boolean sortOrder, Map<String, String> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> criteria = cb.createQuery(type);

        Root<T> root = criteria.from(type);
        if (sortField != null && !sortField.isEmpty() && sortOrder != null) {
            criteria.orderBy(
                    (sortOrder ?
                            cb.asc(root.get(sortField)) :
                            cb.desc(root.get(sortField)))
            );
        }

        if (filters != null && !filters.isEmpty()) {
            Predicate[] restrinctions = getPredicates(filters, cb, root);
            criteria.where(restrinctions);
        }

        TypedQuery<T> tq = em.createQuery(criteria);
        tq.setFirstResult(first);
        tq.setMaxResults(pageSize);

        List<T> result = tq.getResultList();

        return result;
    }

    private Predicate[] getPredicates(Map<String, String> filters, CriteriaBuilder cb, Root root) {
        Set<String> filterKeys = filters.keySet();
        Predicate[] restrinctions = new Predicate[filterKeys.size()];
        int i = 0;
        for (String currentKey : filterKeys) {
            restrinctions[i] = cb.like(root.get(currentKey), filters.get(currentKey));
            i++;
        }
        return restrinctions;
    }

    /**
     * @param filters set of filtering restrictions
     */
    public int getCount(Map<String, String> filters) {
        CriteriaBuilder qb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        cq.select(qb.count(cq.from(type)));
        if (filters != null && !filters.isEmpty()) {
            Predicate[] restrinctions = getPredicates(filters, em.getCriteriaBuilder(), cq.from(type));
            cq.where(restrinctions);
        }
        Long singleResult = em.createQuery(cq).getSingleResult();
        return singleResult != null ? singleResult.intValue() : 0;
    }

    public int getTotalCount() {
        return getCount(null);
    }

    /**
     * It looks for a entity by example
     *
     * @param example example entity
     */
    // FIXME: Применять с осторожностью, лучше только для тестов! query.getResultList() возвращает только коллекцию из одного элемента!
    @Override
    public T findByExample(T example) {
        // Create a native EclipseLink query using QBE policy
        QueryByExamplePolicy policy = new QueryByExamplePolicy();
        policy.excludeDefaultPrimitiveValues();
        ReadObjectQuery q = new ReadObjectQuery(example, policy);

        // Wrap the native query in a standard JPA Query and execute it
        Query query = JpaHelper.createQuery(q, em);
        try {
            return (T) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    /**
     *
     */
    public T merge(T entity) {
        return em.merge(entity);
    }


    public void saveOrUpdate(T entity) {
        merge(entity);
    }


    /**
     * makes  a  transient  instance  persistent.  However,  it  does  not  guarantee  that  the
     * identifier value will be assigned to the persistent instance immediately, the assignment might
     * happen at flush time
     *
     * @param entity entity to be persist
     */
    public void persist(T entity) {
        em.persist(entity);
    }

    public void delete(T entity) {
        final T managedEntity = find(entity.getId());
        if (managedEntity != null) {
            em.remove(managedEntity);
        }
    }


    /**
     *
     */
    public <T> List<T> execSQL(String sql, Object... params) {
        return null;//DbExecuteUtils.execSQL(sql, em, params);
    }

    public String getModuleVersion() {
        return getClass().getPackage().getImplementationVersion();
    }

    public String getBuildDate() {
        return getClass().getPackage().getSpecificationTitle();
    }

    /**
     * Клонирование Entity
     *
     * @param entity
     * @return
     */
    public T clone(T entity) {
        CopyGroup group = new CopyGroup();
        group.setShouldResetPrimaryKey(true);
        group.setShouldResetVersion(true);

        T copy = (T) em.unwrap(JpaEntityManager.class).copy(entity, group);

        return copy;
    }
}
