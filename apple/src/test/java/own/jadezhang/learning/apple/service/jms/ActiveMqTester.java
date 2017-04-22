package own.jadezhang.learning.apple.service.jms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Zhang Junwei on 2017/4/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class ActiveMqTester {
    @Autowired
    private QueueMsgSender queueMsgSender;

    @Test
    public void testActiveMQCluster() throws Exception {
        //一千万条消息以每隔0.3秒频率发送
        long count = 100000000;
        for (int i = 0; i < count; i++) {
            queueMsgSender.send("test.queue", "[" + i + "] this is a quent message");
            Thread.sleep(300);
        }
    }
}
