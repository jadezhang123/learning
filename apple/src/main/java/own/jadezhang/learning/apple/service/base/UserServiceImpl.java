package own.jadezhang.learning.apple.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import own.jadezhang.common.service.AbstractServiceImpl;
import own.jadezhang.learning.apple.dao.base.IUserDAO;
import own.jadezhang.learning.apple.domain.base.User;

/**
 * Created by Zhang Junwei on 2016/10/16.
 */
@Service
public class UserServiceImpl extends AbstractServiceImpl<IUserDAO, User> implements IUserService<IUserDAO, User>{
    @Autowired
    private IUserDAO userDAO;
    @Override
    public IUserDAO getDao() {
        return userDAO;
    }

}
