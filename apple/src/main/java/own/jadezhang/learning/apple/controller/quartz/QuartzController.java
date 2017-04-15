package own.jadezhang.learning.apple.controller.quartz;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import own.jadezhang.common.domain.common.ResultDTO;
import own.jadezhang.learning.apple.domain.quartz.QuartzJob;
import own.jadezhang.learning.apple.domain.quartz.ScheduleJob;
import own.jadezhang.learning.apple.domain.quartz.SendMailJob;
import own.jadezhang.learning.apple.service.quartz.ScheduleJobManager;

/**
 * Created by Zhang Junwei on 2017/3/26.
 */
@Controller
@RequestMapping("/apple/quartz/test")
public class QuartzController {
    @Autowired
    private ScheduleJobManager scheduleJobManager;

    @ResponseBody
    @RequestMapping(value = "/addJob")
    public void addJob() {
        scheduleJobManager.addJob(ScheduleJob.class, "job1", "test");
    }

    @ResponseBody
    @RequestMapping(value = "/scheduleJob")
    public ResultDTO scheduleJob() {
        try {
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger2", "test")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0/30 * * * * ? "))
                    .startNow()
                    .build();
            scheduleJobManager.scheduleJob(QuartzJob.class, "task2", "system", trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new ResultDTO(false, "失败");
        }
        return new ResultDTO(true, "成功");
    }

    @ResponseBody
    @RequestMapping(value = "/pauseJob")
    public ResultDTO pauseJob() {
        try {
            scheduleJobManager.pauseJob("logTask", "system");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new ResultDTO(false, "失败");
        }
        return new ResultDTO(true, "成功");
    }

    @ResponseBody
    @RequestMapping(value = "/resumeJob")
    public ResultDTO resumeJob() {
        try {
            scheduleJobManager.resumeJob("logTask", "system");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new ResultDTO(false, "失败");
        }
        return new ResultDTO(true, "成功");
    }

    @ResponseBody
    @RequestMapping(value = "/pauseTrigger")
    public ResultDTO pauseTrigger() {

        try {
            scheduleJobManager.pauseTrigger("logTaskTrigger", "systemTrigger");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new ResultDTO(false, "失败");
        }
        return new ResultDTO(true, "成功");
    }

    @ResponseBody
    @RequestMapping(value = "/resumeTrigger")
    public ResultDTO resumeTrigger() {
        try {
            scheduleJobManager.resumeTrigger("logTaskTrigger", "systemTrigger");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new ResultDTO(false, "失败");
        }
        return new ResultDTO(true, "成功");
    }

    @ResponseBody
    @RequestMapping(value = "/sendMail")
    public ResultDTO sendMail() {

        try {
            scheduleJobManager.addJob(SendMailJob.class, "sendMail", "test");
            JobDataMap dataMap = new JobDataMap();
            dataMap.put("sender", "jack");
            dataMap.put("receiver", "lucy");
            dataMap.put("content", "i love you");
            scheduleJobManager.triggerJob("sendMail", "test", dataMap);

            Thread.sleep(2000);

            JobDataMap dataMap1 = new JobDataMap();
            dataMap1.put("sender", "lucy");
            dataMap1.put("receiver", "jack");
            dataMap1.put("content", "i love you too");
            scheduleJobManager.triggerJob("sendMail", "test", dataMap1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultDTO(true, "成功");
    }
}
