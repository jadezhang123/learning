package own.jadezhang.learning.apple.controller.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import own.jadezhang.learning.apple.domain.base.User;
import own.jadezhang.learning.apple.service.base.IUserService;

import java.util.List;

/**
 * Created by Zhang Junwei on 2016/10/16.
 */
@Controller
@RequestMapping("/apple/base/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @RequestMapping("/findAll")
    @ResponseBody
    public List<User> findAll(){
        return userService.findAll();
    }
}
