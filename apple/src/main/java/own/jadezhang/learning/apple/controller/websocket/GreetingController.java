package own.jadezhang.learning.apple.controller.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import own.jadezhang.learning.apple.domain.base.User;

/**
 * Created by Zhang Junwei on 2017/4/8.
 */
@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String handle(User user) {
        return "Hello, " + user.getName();
    }
}
