package own.jadezhang.learning.apple.service.quartz.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * Created by Zhang Junwei on 2017/3/26.
 */
@Component("jobProcessListener")
public class JobProcessListener extends JobListenerAdapter{

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException){
        Object result = context.getResult();
    }
}
