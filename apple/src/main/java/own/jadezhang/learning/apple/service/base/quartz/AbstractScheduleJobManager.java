package own.jadezhang.learning.apple.service.base.quartz;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by Zhang Junwei on 2016/11/21.
 */
public abstract class AbstractScheduleJobManager implements ApplicationContextAware, ScheduleJobManager{

    private static ApplicationContext applicationContext;
    private List<ScheduleJobListener> scheduleJobListeners;
    protected String schedulerFactory;
    protected String triggerKey;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setSchedulerFactory(String schedulerFactory) {
        Assert.notNull(schedulerFactory, "Scheduler factory can not be null");
        this.schedulerFactory = schedulerFactory;
    }

    public void setTriggerKey(String triggerKey) {
        Assert.notNull(triggerKey, "Trigger key can not be null");
        this.triggerKey = triggerKey;
    }


    protected Object getObject(String name){
        return applicationContext.getBean(name);
    }
}
