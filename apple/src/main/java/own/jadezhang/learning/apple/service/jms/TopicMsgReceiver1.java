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
@Component("topicMsgReceiver1")
public class TopicMsgReceiver1 implements MessageListener{
    private static final Logger logger = LoggerFactory.getLogger(TopicMsgReceiver1.class);
    @Override
    public void onMessage(Message message) {
        if (message instanceof ObjectMessage){
            try {
                User user = (User) ((ObjectMessage) message).getObject();
                logger.debug("topicMsgReceiver1 receive : "+user);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
