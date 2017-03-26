package own.jadezhang.learning.apple.service.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;

/**
 * Created by Zhang Junwei on 2017/3/25.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class StatefulMethodInvokingJob extends MethodInvokingJob {
    // No implementation, just an addition of the tag interface StatefulJob
    // in order to allow stateful method invoking jobs.
}
