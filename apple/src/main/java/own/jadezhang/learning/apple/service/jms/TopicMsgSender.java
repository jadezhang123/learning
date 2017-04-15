package own.jadezhang.learning.apple.service.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import own.jadezhang.learning.apple.domain.base.User;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

/**
 * Created by Zhang Junwei on 2017/4/11.
 */
@Component
public class TopicMsgSender {

    @Autowired
    @Qualifier("jmsTopicTemplate")
    private JmsTemplate jmsTemplate;

    /**
     * 发送一条消息到指定的topic（目标）
     * @param topicName topic名称
     * @param user   消息内容
     */
    public void send(String topicName, final User user) {
        jmsTemplate.send(topicName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage objectMessage = session.createObjectMessage(user);
                return objectMessage;
            }
        });
    }
}
