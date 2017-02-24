package own.jadezhang.learning.apple.service.base;

import own.jadezhang.common.dao.IBaseDAO;
import own.jadezhang.common.domain.BaseDomain;
import own.jadezhang.common.service.IBaseService;
import own.jadezhang.common.service.IPagingService;
import own.jadezhang.learning.apple.domain.base.UserEx;

import java.util.List;
import java.util.Map;

/**
 * Created by Zhang Junwei on 2016/10/16.
 */
public interface IUserService<D extends IBaseDAO, T extends BaseDomain> extends IBaseService<D, T>, IPagingService<D, T> {
    int deleteUser(Long id);

    /**
     * 查询用户，附带关联的文章
     * @param condition
     * @return
     */
    List<UserEx> getUserWithArticles(Map<String, Object> condition);
}
