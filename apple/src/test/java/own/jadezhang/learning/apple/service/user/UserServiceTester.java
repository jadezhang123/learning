package own.jadezhang.learning.apple.service.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import own.jadezhang.learning.apple.service.base.IUserService;

/**
 * Created by Zhang Junwei on 2017/4/21 0021.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class UserServiceTester {
    @Autowired
    private IUserService userService;

    @Test
    public void testUpdateCount() throws Exception {
        userService.updateCount("c");
    }
}
