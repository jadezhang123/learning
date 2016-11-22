package own.jadezhang.learning.apple.domain.quartz;

import own.jadezhang.common.domain.CreateBaseDomain;
import own.jadezhang.learning.apple.service.base.quartz.ScheduleJobListener;

/**
 * Created by Zhang Junwei on 2016/11/21.
 */
public class ScheduleJob extends CreateBaseDomain<Long>{
    private String code;
    private String name;
    private String groupCode;
    private String cronExpression;
    private int type;
    //处理状态 0-待处理，1-处理中，2-处理成功，3-处理失败，4-取消处理
    private Integer dealStatus;
    private String dealDesc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(Integer dealStatus) {
        this.dealStatus = dealStatus;
    }

    public String getDealDesc() {
        return dealDesc;
    }

    public void setDealDesc(String dealDesc) {
        this.dealDesc = dealDesc;
    }
}
