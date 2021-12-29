package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/14 20:06
 */
@Data
public class HmeNonStandardDetailsVO implements Serializable {

    private static final long serialVersionUID = 8221837947180377432L;

    @ApiModelProperty(value = "工单id")
    private String workOrderId;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "EO编码")
    private String eoNum;

    @ApiModelProperty(value = "SN")
    private String identification;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date creationDate;

    @ApiModelProperty(value = "EO执行作业状态")
    private String eoStatus;

    @ApiModelProperty(value = "当前工序")
    private String processName;

    @ApiModelProperty(value = "当前工序滞留时间")
    private String delayTime;

    @ApiModelProperty(value = "计划完工时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planStartTime;

    @ApiModelProperty(value = "实际完工时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualEndTime;

}
