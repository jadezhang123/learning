package own.jadezhang.common.service;

import own.jadezhang.common.dao.IBaseDAO;
import own.jadezhang.common.domain.BaseDomain;

import java.util.List;
import java.util.Map;

/**
 * Created by Zhang Junwei on 2016/10/16.
 */
public abstract class BaseServiceImpl<D extends IBaseDAO, T extends BaseDomain> implements IBaseService<D, T> {
    @Override
    public int add(T entity) {
        return getDao().insert(entity);
    }

    @Override
    public int batchAdd(List<T> entities) {
        if (entities != null && entities.size() > 0) {
            return getDao().batchInsert(entities);
        }
        return 0;
    }

    @Override

    public int update(T entity) {
        return getDao().update(entity);
    }

    @Override
    public int updateMap(Map<String, Object> entityMap) {
        return getDao().updateMap(entityMap);
    }

    @Override
    public int deleteById(Object id) {
        return getDao().deleteById(id);
    }

    @Override
    public int deleteByIds(List ids) {
        return getDao().deleteByIds(ids);
    }

    @Override
    public int deleteByProperty(String property, Object value) {
        return getDao().deleteByProperty(property, value);
    }

    @Override
    public int deleteByCondition(Map<String, Object> condition) {
        return getDao().deleteByCondition(condition);
    }

    @Override
    public T fetch(Object id) {
        return (T) getDao().fetch(id);
    }

    @Override
    public T findOne(String property, Object value, String orderBy, String sortBy) {
        return (T) getDao().findOne(property, value, orderBy, sortBy);
    }

    @Override
    public List<T> findList(String property, Object value, String orderBy, String sortBy) {
        return getDao().findList(property, value, orderBy, sortBy);
    }

    @Override
    public List<T> findAll() {
        return getDao().findAll(null, null);
    }

    @Override
    public List<T> findAll(String orderBy, String sortBy) {
        return getDao().findAll(orderBy, sortBy);
    }

    @Override
    public T queryOne(Map<String, Object> condition, String orderBy, String sortBy) {
        return (T) getDao().queryOne(condition, orderBy, sortBy);
    }

    @Override
    public List<T> queryList(Map<String, Object> condition, String orderBy, String sortBy) {
        return getDao().queryList(condition, orderBy, sortBy);
    }

    @Override
    public List<T> like(Map<String, Object> condition, String orderBy, String sortBy) {
        return getDao().like(condition, orderBy, sortBy);
    }

    @Override
    public int count(Map<String, Object> condition) {
        return getDao().count(condition);
    }

    @Override
    public List<T> queryForPage(Map<String, Object> condition, Integer pageNo, Integer pageSize) {
        return getDao().queryForPage(condition, null, null, (pageNo - 1) * pageSize, pageSize);
    }

    @Override
    public List<T> queryForPage(Map<String, Object> condition, String orderBy, String sortBy, Integer pageNo, Integer pageSize) {
        return getDao().queryForPage(condition, orderBy, sortBy, (pageNo - 1) * pageSize, pageSize);
    }
}
