package own.jadezhang.learning.apple.service.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by Zhang Junwei on 2017/4/11.
 */
@Component("queueMsgReceiver")
public class QueueMsgReceiver implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(QueueMsgReceiver.class);

    @Override
    public void onMessage(Message message) {
        TextMessage msg = (TextMessage) message;
        System.out.println(msg);
        try {
            logger.debug("QueueMsgReceiver receive a message:" + msg.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
