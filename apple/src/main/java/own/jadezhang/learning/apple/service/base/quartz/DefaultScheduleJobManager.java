package own.jadezhang.learning.apple.service.base.quartz;

import org.quartz.*;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import own.jadezhang.learning.apple.domain.quartz.CronJobBean;
import own.jadezhang.learning.apple.domain.quartz.QuartzJob;
import own.jadezhang.learning.apple.domain.quartz.ScheduleJob;

/**
 * Created by Zhang Junwei on 2016/11/21.
 */
public class DefaultScheduleJobManager extends AbstractScheduleJobManager{

    @Override
    public int addJob(ScheduleJob job) {
        try {
            SchedulerFactoryBean schedulerFactory = (SchedulerFactoryBean)getObject(this.schedulerFactory);

            JobDetailFactoryBean jobDetailFactoryBean = (JobDetailFactoryBean) getObject("");
            jobDetailFactoryBean.setJobClass(new Job(){
                @Override
                public void execute(JobExecutionContext context) throws JobExecutionException {

                }
            }.getClass());
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobDetail jobDetail = JobBuilder.newJob(QuartzJob.class)
                            .withIdentity(job.getCode(), job.getGroupCode()).build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("myTrigger", "group1")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(40)
                            .repeatForever())
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {

        }
        return 0;
    }

    @Override
    public boolean resetJob(ScheduleJob job) {
        return false;
    }
}
