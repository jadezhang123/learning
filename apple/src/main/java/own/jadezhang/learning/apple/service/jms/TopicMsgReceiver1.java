package own.jadezhang.learning.apple.service.jms;

import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Created by Zhang Junwei on 2017/4/11.
 */
@Component("topicMsgReceiver1")
public class TopicMsgReceiver1 implements MessageListener{
    @Override
    public void onMessage(Message message) {

    }
}
