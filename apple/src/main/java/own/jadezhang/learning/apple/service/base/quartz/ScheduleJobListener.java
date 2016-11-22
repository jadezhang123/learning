package own.jadezhang.learning.apple.service.base.quartz;

import own.jadezhang.common.domain.common.ResultDTO;
import own.jadezhang.learning.apple.domain.quartz.JobTypeEnum;
import own.jadezhang.learning.apple.domain.quartz.ScheduleJob;

/**
 * Created by Zhang Junwei on 2016/11/21.
 */
public interface ScheduleJobListener {
    JobTypeEnum getJobType();

    ResultDTO dealJob(ScheduleJob job);

    void afterDealJob(ScheduleJob job);

}
