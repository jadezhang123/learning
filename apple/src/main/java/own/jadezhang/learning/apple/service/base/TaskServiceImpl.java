package own.jadezhang.learning.apple.service.base;

import org.quartz.CronScheduleBuilder;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import own.jadezhang.learning.apple.domain.quartz.NewJob;
import own.jadezhang.learning.apple.domain.quartz.QuartzJob;
import own.jadezhang.learning.apple.service.quartz.ScheduleJobManager;

/**
 * Created by Zhang Junwei on 2017/3/25.
 */
@Service("taskServiceImpl")
public class TaskServiceImpl implements ITaskService {
    @Autowired
    private ScheduleJobManager scheduleJobManager;

    public static final String JobName = "logTask";
    public static final String Group = "system";
    public static final String TriggerName = "logTaskTrigger";
    public static final String TriggerGroup = "systemTrigger";

    @Override
    public void addLogTask() {
        //scheduleJobManager.addJob(RepeatJobBean.class, JobName, Group);
        //scheduleJobManager.addJob(CronJobBean.class, "task1", Group);
        scheduleJobManager.addJob(QuartzJob.class, "task2", Group);
    }

    @Override
    public void triggerLogTask() {
        try {
            scheduleJobManager.triggerJob("task1", Group);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void scheduleLogTask() {
        try {
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("logTrigger3", TriggerGroup)
                    .withSchedule(CronScheduleBuilder.cronSchedule("").withMisfireHandlingInstructionDoNothing())
                    .startNow()
                    .build();
            scheduleJobManager.scheduleJob(NewJob.class, "newjob", "system", trigger);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
