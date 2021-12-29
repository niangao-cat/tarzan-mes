package io.tarzan.common.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

public class MtNumrangeAssignHisDTO implements Serializable {

    private static final long serialVersionUID = 4582383458900328677L;

    @ApiModelProperty(value = "编码对象ID")
    private String objectId;

    @ApiModelProperty(value = "号段组号")
    private List<String> numrangeGroupList;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public List<String> getNumrangeGroupList() {
        return numrangeGroupList;
    }

    public void setNumrangeGroupList(List<String> numrangeGroupList) {
        this.numrangeGroupList = numrangeGroupList;
    }

    public Date getStartTime() {
        if (startTime == null) {
            return null;
        }
        return (Date) this.startTime.clone();

    }

    public void setStartTime(Date startTime) {
        if (startTime == null) {
            this.startTime = null;
        } else {
            this.startTime = (Date) startTime.clone();
        }
    }

    public Date getEndTime() {
        if (endTime == null) {
            return null;
        }
        return (Date) this.endTime.clone();
    }

    public void setEndTime(Date endTime) {
        if (endTime == null) {
            this.endTime = null;
        } else {
            this.endTime = (Date) endTime.clone();
        }
    }

}
