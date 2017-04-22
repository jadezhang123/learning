package own.jadezhang.learning.apple.service.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import own.jadezhang.learning.apple.domain.base.User;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * Created by Zhang Junwei on 2017/4/11.
 */
@Component("topicMsgReceiver2")
public class TopicMsgReceiver2 implements MessageListener{
    private static final Logger logger = LoggerFactory.getLogger(TopicMsgReceiver2.class);
    @Override
    public void onMessage(Message message) {
        if (message instanceof ObjectMessage){
            try {
                User user = (User) ((ObjectMessage) message).getObject();
                logger.debug("topicMsgReceiver2 receive : "+user);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
