package own.jadezhang.learning.apple.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import own.jadezhang.common.service.AbstractServiceImpl;
import own.jadezhang.learning.apple.dao.base.IArticleDAO;
import own.jadezhang.learning.apple.dao.base.IUserDAO;
import own.jadezhang.learning.apple.dao.redis.IRedisRepository;
import own.jadezhang.learning.apple.domain.base.User;
import own.jadezhang.learning.apple.domain.base.UserEx;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhang Junwei on 2016/10/16.
 */
@Service
//@Transactional
public class UserServiceImpl extends AbstractServiceImpl<IUserDAO, User> implements IUserService<IUserDAO, User> {

    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private IRedisRepository<String, String> redisRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private IUserDAO userDAO;
    @Autowired
    private IArticleDAO articleDAO;

    @Override
    public IUserDAO getDao() {
        return userDAO;
    }


    @Override
    public Integer deleteUser(final Long id) {

        Integer count = transactionTemplate.execute(new TransactionCallback<Integer>() {
            @Override
            public Integer doInTransaction(TransactionStatus status) {
                int count = articleDAO.deleteByProperty("uid", id);
                count += userDAO.deleteById(id);
                return count;
            }
        });
        return count;
    }

    @Override
    public void cacheUser(String userCode, String userName) {
        redisRepository.set(userCode, userName);
    }

    @Override
    public String getCachedUser(String userCode) {
        return redisRepository.get(userCode);
    }

    @Override
    public boolean updateCount(String code) {
        redisTemplate.watch(code);
        redisTemplate.multi();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //在此期间如果code的值改变则事物执行失败
        redisRepository.incr(code, 1);
        List<Object> execResult = redisTemplate.exec();
        if (execResult == null || execResult.size() == 0){
            System.out.println("事务执行失败");
        }
        return false;
    }


    @Override
    public List<UserEx> getUserWithArticles(Map<String, Object> condition) {
        return userDAO.getUserWithArticles(condition);
    }
}
