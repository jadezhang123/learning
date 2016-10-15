package own.jadezhang.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import own.jadezhang.common.dao.IBaseDAO;
import own.jadezhang.common.domain.BaseDomain;

/**
 * Created by Zhang Junwei on 2016/10/16.
 */
@Service
public class BaseServiceImpl implements IBaseService<IBaseDAO, BaseDomain>{
    @Autowired
    private IBaseDAO baseDAO;

    @Override
    public IBaseDAO getDao() {
        return baseDAO;
    }
}
