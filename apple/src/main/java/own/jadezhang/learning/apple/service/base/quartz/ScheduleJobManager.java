package own.jadezhang.learning.apple.service.base.quartz;

import own.jadezhang.learning.apple.domain.quartz.ScheduleJob;

/**
 * Created by Zhang Junwei on 2016/11/21.
 */
public interface ScheduleJobManager {

    int addJob(ScheduleJob job);

    boolean resetJob(ScheduleJob job);

}
