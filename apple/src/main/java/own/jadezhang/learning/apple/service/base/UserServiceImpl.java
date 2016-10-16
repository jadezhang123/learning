package own.jadezhang.learning.apple.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import own.jadezhang.common.service.AbstractServiceImpl;
import own.jadezhang.learning.apple.dao.base.IArticleDAO;
import own.jadezhang.learning.apple.dao.base.IUserDAO;
import own.jadezhang.learning.apple.domain.base.User;

import javax.annotation.Resource;

/**
 * Created by Zhang Junwei on 2016/10/16.
 */
@Service
//@Transactional
public class UserServiceImpl extends AbstractServiceImpl<IUserDAO, User> implements IUserService<IUserDAO, User> {

    @Resource
    private TransactionTemplate transactionTemplate;

    @Autowired
    private IUserDAO userDAO;
    @Autowired
    private IArticleDAO articleDAO;

    @Override
    public IUserDAO getDao() {
        return userDAO;
    }


    @Override
    public int deleteUser(final Long id) {

        int count = transactionTemplate.execute(new TransactionCallback<Integer>() {
            @Override
            public Integer doInTransaction(TransactionStatus status) {
                int count = articleDAO.deleteByProperty("uid", id);
                count += userDAO.deleteById(id);
                return count;
            }
        });
        return count;
    }
}
