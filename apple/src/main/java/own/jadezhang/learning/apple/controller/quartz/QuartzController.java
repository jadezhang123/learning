package own.jadezhang.learning.apple.controller.quartz;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import own.jadezhang.common.domain.common.ResultDTO;
import own.jadezhang.learning.apple.domain.quartz.ScheduleJob;
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
            Scheduler scheduler = scheduleJobManager.getScheduler();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger1","test")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0/15 * * * * ? "))
                    .startNow()
                    .forJob("task2", "system")
                    .build();
            scheduler.scheduleJob(trigger);
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
}
