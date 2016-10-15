package own.jadezhang.common.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import own.jadezhang.common.domain.BaseDomain;

import java.util.List;
import java.util.Map;

/**
 * Created by Zhang Junwei on 2016/10/16.
 */
@Repository
public interface IBaseDAO<T extends BaseDomain> {
    int insert(T entity);

    int update(T entity);

    int updateMap(@Param("map") Map<String, Object> entityMap);

    int deleteById(@Param("id") Object id);

    int deleteByIds(@Param("ids") List ids);

    int deleteByCondition(@Param("condition") Map<String, Object> condition);

    T findOne(@Param("property") String property, @Param("value") Object value, @Param("orderBy") String orderBy, @Param("sortBy") String sortBy);

    List<T> findList(@Param("property") String property, @Param("value") Object value, @Param("orderBy") String orderBy, @Param("sortBy") String sortBy);

    List<T> findAll(@Param("orderBy") String orderBy, @Param("sortBy") String sortBy);


    T queryOne(@Param("condition") Map<String, Object> condition, @Param("orderBy") String orderBy, @Param("sortBy") String sortBy);

    List<T> queryList(@Param("condition") Map<String, Object> condition, @Param("orderBy") String orderBy, @Param("sortBy") String sortBy);

    List<T> queryAll(@Param("orderBy") String orderBy, @Param("sortBy") String sortBy);

    List<T> paging(@Param("condition") Map<String, Object> condition, @Param("orderBy") String orderBy, @Param("sortBy") String sortBy,
                      @Param("offset") int offset, @Param("rows") int rows);


}
