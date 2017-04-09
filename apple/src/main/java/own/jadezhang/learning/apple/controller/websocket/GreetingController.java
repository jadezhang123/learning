package own.jadezhang.learning.apple.controller.websocket;

import com.alibaba.fastjson.JSON;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import own.jadezhang.learning.apple.domain.base.Article;
import own.jadezhang.learning.apple.domain.base.User;

/**
 * Created by Zhang Junwei on 2017/4/8.
 */
@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Article hello(User user) {
        Article article = new Article();
        article.setName("learning" + user.getName());
        article.setComment("this is a good article");
        return article;
    }

    @MessageMapping("/hello1")
    @SendTo("/topic/task")
    public String hello1(User user) {
        Article article = new Article();
        article.setName("learning" + user.getName());
        article.setComment("this is a good article");
        return JSON.toJSONString(article);
    }


}
