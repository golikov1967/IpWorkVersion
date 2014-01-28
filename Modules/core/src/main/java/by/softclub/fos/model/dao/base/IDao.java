package by.softclub.fos.model.dao.base;

import java.io.Serializable;
import java.rmi.Remote;
import java.util.List;
import java.util.Map;

/**
 * Interface for a Dao-class
 * @author shum
 *
 * @param <T> - entity type
 * @param <PK> - type of primary key of entity
 */
public interface IDao <T extends Remote, PK extends Serializable> extends Serializable{
	int getCount(Map<String, String> filters);

    /**
     * Поиск сущности по первичному ключу
     * @param id
     * @return
     */
    T find(PK id);

	List<T> findAll();

    List<T> readPage(int first, int pageSize, String sortField, Boolean sortOrder, Map<String,String> filters);
	
	T findByExample(T example);
	
	T merge(T entity);

	void persist(T entity);

	void delete(T entity);

    int getTotalCount();

    void saveOrUpdate(T selectedUser);

    <T> List<T> execSQL(String sql, Object... params);
}