package own.jadezhang.learning.apple.service.base;

import own.jadezhang.common.dao.IBaseDAO;
import own.jadezhang.common.domain.BaseDomain;
import own.jadezhang.common.service.IBaseService;
import own.jadezhang.common.service.IPagingService;

/**
 * Created by Zhang Junwei on 2016/10/16.
 */
public interface IArticleService<D extends IBaseDAO, T extends BaseDomain> extends IBaseService<D, T>, IPagingService<D, T> {
}
