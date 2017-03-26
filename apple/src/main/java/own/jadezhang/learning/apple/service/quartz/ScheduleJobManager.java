package own.jadezhang.learning.apple.service.quartz;

import org.quartz.*;

/**
 * Created by Zhang Junwei on 2016/11/21.
 */
public interface ScheduleJobManager {

    boolean checkExist(String jobName, String group) throws SchedulerException;

    boolean addJob(JobDetail job);

    /**
     * 添加任务（属于默认分组）
     *
     * @param jobClass 执行任务的job
     * @param jobName
     */
    boolean addJob(Class<? extends Job> jobClass, String jobName);

    /**
     * 添加任务
     *
     * @param jobClass 执行任务的job
     * @param jobName
     * @param group
     */
    boolean addJob(Class<? extends Job> jobClass, String jobName, String group);

    /**
     * 添加方法调用类型的无状态任务（可并行执行）
     *
     * @param targetObject spring容器中目标实例的id，目标类无须实现org.quartz.Job接口
     * @param targetMethod 目标实例的方法名（该方法为任务的实际主体）
     * @param jobName      任务名称
     * @param group        所属分组；若为默认分组：请传入org.quartz.Scheduler.DEFAULT_GROUP
     */
    boolean addMethodInvokingJob(String targetObject, String targetMethod, String jobName, String group);

    /**
     * 添加方法调用类型的有状态任务（不能并行执行）
     *
     * @param targetObject spring容器中目标实例的id，目标类无须实现org.quartz.Job接口
     * @param targetMethod 目标实例的方法名（该方法为任务的实际主体）
     * @param jobName      任务名称
     * @param group        所属分组；若为默认分组：请传入org.quartz.Scheduler.DEFAULT_GROUP
     */
    boolean addStatefulMethodInvokingJob(String targetObject, String targetMethod, String jobName, String group);

    JobDetail getJobDetail(String jobName, String group) throws SchedulerException;

    /**
     * 触发该任务立即执行（只触发一次），与{@link #addJob(JobDetail)}及其重载方法配合使用
     *
     * @param jobName
     * @param group
     */
    void triggerJob(String jobName, String group) throws SchedulerException;

    /**
     * 触发该任务立即执行（只触发一次），与{@link #addJob(JobDetail)}及其重载方法配合使用
     * 一般此类任务为有状态，不能并行执行
     * @param jobName
     * @param group
     * @param data
     * @throws SchedulerException
     */
    void triggerJob(String jobName, String group, JobDataMap data) throws SchedulerException;

    /**
     * 安排任务（任务必须已经存在，否则忽略），与{@link #addJob(JobDetail)}及其重载方法配合使用
     *
     * @param jobName
     * @param group
     * @param triggerName
     * @param triggerGroup
     * @param cronExpression 不能为空（null或空字符串），否则无效
     * @throws SchedulerException
     */
    void scheduleJob(String jobName, String group, String triggerName, String triggerGroup, String cronExpression) throws SchedulerException;

    /**
     * 安排任务
     *
     * @param jobClass
     * @param jobName
     * @param group
     * @param triggerName
     * @param triggerGroup
     * @param cronExpression 不能为空（null或空字符串），否则无效
     * @throws SchedulerException 若任务已经存在则抛出异常
     */
    void scheduleJob(Class<? extends Job> jobClass, String jobName, String group, String triggerName, String triggerGroup, String cronExpression) throws SchedulerException;

    /**
     * 安排新任务
     *
     * @param jobClass
     * @param jobName
     * @param group
     * @param trigger
     * @throws SchedulerException 若任务已经存在则抛出异常
     */
    void scheduleJob(Class<? extends Job> jobClass, String jobName, String group, Trigger trigger) throws SchedulerException;


    /**
     * 更新任务的触发器
     *
     * @param triggerName
     * @param triggerGroup
     * @param newTrigger
     * @throws SchedulerException
     */
    void rescheduleJob(String triggerName, String triggerGroup, Trigger newTrigger) throws SchedulerException;

    /**
     * 暂定指定任务
     *
     * @param jobName
     * @param group
     */
    void pauseJob(String jobName, String group) throws SchedulerException;

    void resumeJob(String jobName, String group) throws SchedulerException;

    void pauseTrigger(String triggerName, String triggerGroup) throws SchedulerException;

    void resumeTrigger(String triggerName, String triggerGroup) throws SchedulerException;

    Scheduler getScheduler();

}
