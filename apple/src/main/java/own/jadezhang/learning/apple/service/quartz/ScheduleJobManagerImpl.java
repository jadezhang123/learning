package own.jadezhang.learning.apple.service.quartz;

import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

/**
 * Created by Zhang Junwei on 2016/11/21.
 */
@Component("scheduleJobManager")
public class ScheduleJobManagerImpl implements ScheduleJobManager {
    private static Logger logger = LoggerFactory.getLogger(ScheduleJobManagerImpl.class);
    @Autowired
    private SchedulerFactoryBean schedulerFactory;

    @Override
    public boolean checkExist(String jobName, String group) throws SchedulerException {
        return schedulerFactory.getScheduler().checkExists(this.getJobKey(jobName, group));
    }

    @Override
    public boolean addJob(JobDetail job) {
        Scheduler scheduler = schedulerFactory.getScheduler();
        try {
            scheduler.addJob(job, true);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean addJob(Class<? extends Job> jobClass, String jobName) {
        return this.addJob(jobClass, jobName, Scheduler.DEFAULT_GROUP);
    }


    @Override
    public boolean addJob(Class<? extends Job> jobClass, String jobName, String group) {
        try {
            if (this.checkExist(jobName, group)) {
                //已存在此任务
                return true;
            }
            logger.info("add a job[" + jobClass + "]");
            return this.addJob(buildJobDetail(jobClass, jobName, group, null));
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addMethodInvokingJob(String targetObject, String targetMethod, String jobName, String group) {
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("targetObject", targetObject);
        dataMap.put("targetMethod", targetMethod);
        return this.addJob(buildJobDetail(MethodInvokingJob.class, jobName, group, dataMap));
    }

    @Override
    public boolean addStatefulMethodInvokingJob(String targetObject, String targetMethod, String jobName, String group) {
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("targetObject", targetObject);
        dataMap.put("targetMethod", targetMethod);
        return this.addJob(buildJobDetail(StatefulMethodInvokingJob.class, jobName, group, dataMap));
    }

    @Override
    public JobDetail getJobDetail(String jobName, String group) {
        try {
            return schedulerFactory.getScheduler().getJobDetail(this.getJobKey(jobName, group));
        } catch (SchedulerException e) {
            return null;
        }
    }

    @Override
    public void triggerJob(String jobName, String group) throws SchedulerException {
        schedulerFactory.getScheduler().triggerJob(this.getJobKey(jobName, group));
    }

    @Override
    public void triggerJob(String jobName, String group, JobDataMap data) throws SchedulerException {
        getScheduler().triggerJob(this.getJobKey(jobName, group), data);
    }

    @Override
    public void scheduleJob(String jobName, String group, String triggerName, String triggerGroup, String cronExpression) throws SchedulerException {

        Trigger trigger = buildCronTriggerForJob(jobName, group, triggerName, triggerGroup, cronExpression);

        getScheduler().scheduleJob(trigger);
    }

    @Override
    public void scheduleJob(Class<? extends Job> jobClass, String jobName, String group, String triggerName, String triggerGroup, String cronExpression) throws SchedulerException {

        JobDetail jobDetail = buildJobDetail(jobClass, jobName, group, null);

        Trigger trigger = buildCronTrigger(triggerName, triggerGroup, cronExpression);

        getScheduler().scheduleJob(jobDetail, trigger);
    }

    @Override
    public void scheduleJob(Class<? extends Job> jobClass, String jobName, String group, Trigger trigger) throws SchedulerException {
        JobDetail jobDetail = buildJobDetail(jobClass, jobName, group, null);
        getScheduler().scheduleJob(jobDetail, trigger);
    }

    @Override
    public void rescheduleJob(String triggerName, String triggerGroup, Trigger newTrigger) throws SchedulerException {

    }

    @Override
    public void pauseJob(String jobName, String group) throws SchedulerException {
        getScheduler().pauseJob(this.getJobKey(jobName, group));
    }

    @Override
    public void resumeJob(String jobName, String group) throws SchedulerException {
        getScheduler().resumeJob(this.getJobKey(jobName, group));
    }

    @Override
    public void pauseTrigger(String triggerName, String triggerGroup) throws SchedulerException {
        getScheduler().pauseTrigger(this.getTriggerKey(triggerName, triggerGroup));
    }

    @Override
    public void resumeTrigger(String triggerName, String triggerGroup) throws SchedulerException {
        getScheduler().resumeTrigger(this.getTriggerKey(triggerName, triggerGroup));
    }

    @Override
    public Scheduler getScheduler() {
        return schedulerFactory.getScheduler();
    }

    /**
     * 构造jobDetail实例（将被持久化到数据库）
     *
     * @param jobClass
     * @param jobName
     * @param group
     * @param data     附加数据
     * @return
     */
    private JobDetail buildJobDetail(Class<? extends Job> jobClass, String jobName, String group, JobDataMap data) {
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, group)
                .storeDurably(true)
                .requestRecovery(true)
                .setJobData(data == null ? new JobDataMap() : data)
                .build();
        return jobDetail;
    }

    private Trigger buildCronTrigger(String triggerName, String triggerGroup, String cronExpression) {
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName, triggerGroup)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing())
                .startNow()
                .build();
        return trigger;
    }

    private Trigger buildCronTriggerForJob(String jobName, String group, String triggerName, String triggerGroup, String cronExpression) {
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName, triggerGroup)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing())
                .forJob(jobName, group)
                .startNow()
                .build();
        return trigger;
    }


    private JobKey getJobKey(String jobName, String group) {
        return JobKey.jobKey(jobName, StringUtils.isBlank(group) ? Scheduler.DEFAULT_GROUP : group);
    }

    private TriggerKey getTriggerKey(String triggerName, String triggerGroup) {
        return TriggerKey.triggerKey(triggerName, triggerGroup);
    }

}
