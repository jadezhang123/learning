package own.jadezhang.learning.apple.controller.websocket;

import com.alibaba.fastjson.JSON;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import own.jadezhang.learning.apple.domain.base.Article;
import own.jadezhang.learning.apple.domain.base.User;

/**
 * Created by Zhang Junwei on 2017/4/8.
 */
@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/task")
    public Article hello(User user) {
        Article article = new Article();
        article.setName("learning" + user.getName());
        article.setComment("this is a good article");
        return article;
    }

    /**
     * 针对前端订阅  /user/queue/music-updates
     * 当使用外部的消息代理时，需要配置一旦session无效就要清除用户相关的唯一队列
     * ActiveMQ配置 => http://activemq.apache.org/delete-inactive-destinations.html
     * @param user
     * @return
     */
    @MessageMapping("/music")
    @SendToUser("/queue/music-updates")
    public Article subscribeToUserDestination(User user) {
        Article article = new Article();
        article.setName("learning" + user.getName());
        article.setComment("this is a good article");
        return article;
    }

    @MessageExceptionHandler
    @SendToUser(destinations = "/queue/errors", broadcast = false)
    public String handleException(Throwable exception) {
        Article article = new Article();

        return JSON.toJSONString(article);
    }

}
