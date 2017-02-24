package own.jadezhang.learning.apple.dao.base;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import own.jadezhang.common.dao.IBaseDAO;
import own.jadezhang.learning.apple.domain.base.Article;
import own.jadezhang.learning.apple.domain.base.User;
import own.jadezhang.learning.apple.domain.base.UserEx;

import java.util.List;
import java.util.Map;

/**
 * Created by Zhang Junwei on 2016/10/16.
 */
@Repository
public interface IUserDAO extends IBaseDAO<User> {
    /**
     * 查询用户，附带关联的文章
     * @param condition
     * @return
     */
    List<UserEx> getUserWithArticles(@Param("condition") Map<String, Object> condition);
}
