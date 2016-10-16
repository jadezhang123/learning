package own.jadezhang.common.service;

import own.jadezhang.common.dao.IBaseDAO;
import own.jadezhang.common.domain.BaseDomain;

/**
 * Created by Zhang Junwei on 2016/10/16.
 */
public abstract class AbstractServiceImpl<D extends IBaseDAO, T extends BaseDomain> extends BaseServiceImpl<D, T> implements IPagingService<D, T>{
}
