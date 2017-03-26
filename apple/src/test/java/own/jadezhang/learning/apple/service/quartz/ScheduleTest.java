package own.jadezhang.learning.apple.service.quartz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Zhang Junwei on 2017/3/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class ScheduleTest {
    @Autowired
    private ScheduleJobManager scheduleJobManager;
    @Test
    public void testPauseJob() throws Exception {
    }
}
