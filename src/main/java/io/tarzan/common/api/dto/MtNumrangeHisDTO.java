package io.tarzan.common.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtNumrangeHisDTO implements Serializable {
    private static final long serialVersionUID = 1468199387119990909L;

    @ApiModelProperty(value = "对象ID", required = true)
    private String objectId;
    @ApiModelProperty(value = "号段组号", required = true)
    private String numrangeGroup;
    @ApiModelProperty(value = "查询事件时间开始", required = true)
    private String startTime;
    @ApiModelProperty(value = "查询事件时间结束", required = true)
    private String endTime;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getNumrangeGroup() {
        return numrangeGroup;
    }

    public void setNumrangeGroup(String numrangeGroup) {
        this.numrangeGroup = numrangeGroup;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
