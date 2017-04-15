package own.jadezhang.learning.apple.controller.base;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import own.jadezhang.common.domain.BizData4Page;
import own.jadezhang.common.domain.common.ResultDTO;
import own.jadezhang.common.domain.enums.ReturnCodeEnum;
import own.jadezhang.learning.apple.config.Configurations;
import own.jadezhang.learning.apple.domain.base.Article;
import own.jadezhang.learning.apple.domain.base.User;
import own.jadezhang.learning.apple.domain.base.UserEx;
import own.jadezhang.learning.apple.service.base.IArticleService;
import own.jadezhang.learning.apple.service.base.ITaskService;
import own.jadezhang.learning.apple.service.base.IUserService;
import own.jadezhang.learning.apple.service.jms.QueueMsgSender;
import own.jadezhang.learning.apple.service.jms.TopicMsgSender;
import own.jadezhang.learning.apple.view.base.DetailExcelView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhang Junwei on 2016/10/16.
 */
@Controller
@RequestMapping("/apple/base/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private IUserService userService;
    @Autowired
    private IArticleService articleService;
    @Autowired
    private ITaskService taskService;
    @Autowired
    private QueueMsgSender queueMsgSender;
    @Autowired
    private TopicMsgSender topicMsgSender;
    @ResponseBody
    @RequestMapping(value = "/pagingUsers")
    public BizData4Page<User> pagingUsers(int sex, @RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("sex", sex);
        int records = userService.count(condition);
        if (records == 0) {
            return BizData4Page.forNoRecords(pageSize);
        }
        List<User> list = userService.queryForPage(condition, pageNo, pageSize);
        return BizData4Page.page(list, records, pageNo, pageSize);
    }

    @RequestMapping("/findAll")
    @ResponseBody
    public List<UserEx> findAll() {
        logger.info("findAll users");
        return userService.findAll();
    }

    @RequestMapping(value = "/findUsers")
    @ResponseBody
    public List<UserEx> findUsers(String userName) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("name", userName);
        return userService.getUserWithArticles(condition);
    }

    @RequestMapping("/add")
    @ResponseBody
    public ResultDTO addUser(User user) {
        userService.add(user);
        queueMsgSender.send("test.queue", "this is a quent message");
        topicMsgSender.send("test.topic", user);
        return new ResultDTO(true, ReturnCodeEnum.ADD_COMPLETE.getMessage());
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
        Integer count = userService.deleteUser(id);
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

    @RequestMapping("/saveFile")
    @ResponseBody
    public String saveFile(String token, String ext) {
        String fileUrl = Configurations.copyTempToRepository(token, ext);
        return fileUrl;
    }

    @ResponseBody
    @RequestMapping(value = "/cacheUser")
    public ResultDTO cacheUser(User user) {
        String userCode = "id"+user.getId();
        String cachedUser = userService.getCachedUser(userCode);
        if (StringUtils.isBlank(cachedUser)){
            userService.cacheUser(userCode, user.getName());
            cachedUser = userService.getCachedUser(userCode);
        }
        return new ResultDTO(true, "保存成功" + cachedUser);
    }
    @ResponseBody
    @RequestMapping(value = "/addArticle")
    public ResultDTO addArticle(Article article) {
        try {
            int count = articleService.add(article);
            if (count >0){
                return new ResultDTO(true, ReturnCodeEnum.ADD_COMPLETE.getMessage());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResultDTO(false, ReturnCodeEnum.ACTION_FAILURE.getMessage());
    }


}
