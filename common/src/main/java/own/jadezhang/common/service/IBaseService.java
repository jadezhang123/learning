package own.jadezhang.common.service;

import own.jadezhang.common.dao.IBaseDAO;
import own.jadezhang.common.domain.BaseDomain;

import java.util.List;
import java.util.Map;

/**
 * Created by Zhang Junwei on 2016/10/16.
 */
public interface IBaseService<D extends IBaseDAO, T extends BaseDomain> {
    D getDao();

    int add(T entity);

    int batchAdd(List<T> entities);

    int update(T entity);

    int updateMap(Map<String, Object> entityMap);

    /**
     * 更新实体不为null的字段，实体id必传
     * @param entity
     * @return
     */
    int updateNotNull(T entity);

    int updateByCondition(Map<String, Object> updateMap, Map<String, Object> conditionMap);

    int deleteById(Object id);

    int deleteByIds(List ids);

    int deleteByProperty(String property, Object value);

    int deleteByCondition(Map<String, Object> condition);

    T fetch(Object id);

    T findOne(String property, Object value, String orderBy, String sortBy);

    List<T> findList(String property, Object value, String orderBy, String sortBy);

    List<T> findAll();

    List<T> findAll(String orderBy, String sortBy);

    T queryOne(Map<String, Object> condition, String orderBy, String sortBy);

    List<T> queryList(Map<String, Object> condition, String orderBy, String sortBy);

    List<T> like(Map<String, Object> condition, String orderBy, String sortBy);

    int count(Map<String, Object> condition);

    List<T> queryForPage(Map<String, Object> condition, Integer pageNo, Integer pageSize);

    List<T> queryForPage(Map<String, Object> condition, String orderBy, String sortBy, Integer pageNo, Integer pageSize);


}
