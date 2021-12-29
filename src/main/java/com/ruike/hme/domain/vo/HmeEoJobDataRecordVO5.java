package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobDataRecordVO5 implements Serializable {
    private static final long serialVersionUID = -3707965933812850831L;
    @ApiModelProperty(value = "执行作业ID")
    private String eoId;
    @ApiModelProperty(value = "数据项ID")
    private String tagId;
    @ApiModelProperty(value = "数据组ID")
    private String tagGroupId;
    @ApiModelProperty(value = "工艺ID")
    private String operationId;
    @ApiModelProperty(value = "采集结果")
    private String result;
    @ApiModelProperty(value = "最后更新时间")
    private Date lastUpdateDate;
}
