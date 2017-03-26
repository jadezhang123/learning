package own.jadezhang.learning.apple.service.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;

/**
 * Created by Zhang Junwei on 2017/3/25.
 */
public class MethodInvokingJob extends QuartzJobBean {

    protected static final Logger logger = LoggerFactory.getLogger(MethodInvokingJob.class);

    private String targetObject;
    private String targetMethod;
    private ApplicationContext ctx;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            logger.info("executing the method[" + targetMethod + "] of " + targetObject);

            Object targetBean = ctx.getBean(targetObject);

            if (targetBean == null) {
                throw new JobExecutionException("can not find the bean named " +targetBean+ " in Spring ApplicationContext");
            }

            Method method = null;
            try {
                //只支持无参方法
                method = targetBean.getClass().getMethod(targetMethod, new Class[]{});
                Object result = method.invoke(targetBean, new Object[]{});
                if (result != null) {
                    //如果方法有返回值则将其设到任务执行上下文中，便于TriggerListener和JobListener获取更多的信息
                    context.setResult(result);
                }
            } catch (SecurityException e) {
                logger.error("SecurityException ", e);
            } catch (NoSuchMethodException e) {
                logger.error("NoSuchMethodException ", e);
            }
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    public void setTargetObject(String targetObject) {
        this.targetObject = targetObject;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }
}
