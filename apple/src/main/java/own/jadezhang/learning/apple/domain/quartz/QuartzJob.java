package own.jadezhang.learning.apple.domain.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by Zhang Junwei on 2016/11/21.
 */
public class QuartzJob implements Job{
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("job....");
    }
}
