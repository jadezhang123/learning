package own.jadezhang.learning.apple.controller.base;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import own.jadezhang.common.domain.common.ResultDTO;
import own.jadezhang.common.domain.common.ReturnCodeEnum;
import own.jadezhang.learning.apple.domain.base.User;
import own.jadezhang.learning.apple.service.base.IUserService;
import own.jadezhang.learning.apple.view.base.DetailExcelView;

import javax.jws.soap.SOAPBinding;
import java.util.List;
import java.util.Map;

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
    public List<User> findAll() {
        return userService.findAll();
    }

    @RequestMapping("/batchAdd")
    @ResponseBody
    public ResultDTO addUsers(@RequestBody List<User> users) {
        userService.batchAdd(users);
        return new ResultDTO(true, ReturnCodeEnum.ADD_COMPLETE.getMessage());
    }

    @RequestMapping("/batchAdd1")
    @ResponseBody
    public ResultDTO addUsers(String usersJSONStr) {
        List<User> users = JSON.parseArray(usersJSONStr, User.class);
        System.out.println(userService.batchAdd(users));
        return new ResultDTO(true, ReturnCodeEnum.ADD_COMPLETE.getMessage());
    }

    @RequestMapping("/deleteUser")
    @ResponseBody
    public ResultDTO deleteUser(Long id) {
        int count = userService.deleteUser(id);
        if (count > 0) {
            return new ResultDTO(true, ReturnCodeEnum.DELETE_COMPLETE.getMessage());
        } else {
            return new ResultDTO(false, ReturnCodeEnum.ACTION_FAILURE.getMessage());
        }
    }

    @RequestMapping("/postUser")
    @ResponseBody
    public ResultDTO post(User user) {
        int count = user.getName().equals("jack") ? 1 : 0;
        if (count > 0) {
            return new ResultDTO(true, ReturnCodeEnum.DELETE_COMPLETE.getMessage());
        } else {
            return new ResultDTO(false, ReturnCodeEnum.ACTION_FAILURE.getMessage());
        }
    }

    @RequestMapping("/exportUsers")
    @ResponseBody
    public ModelAndView exportUsers(Map<String, Object> model) {
        List<User> allUsers = userService.findAll();
        model.put("users", allUsers);
        return new ModelAndView(new DetailExcelView(), model);
    }

}
