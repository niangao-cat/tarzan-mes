package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeOperationTimeVO4
 * @author: chaonan.hu@hand-china.com 2020-08-12 13:56:23
 **/
@Data
public class HmeOperationTimeVO4 implements Serializable {
    private static final long serialVersionUID = 3997974563187979913L;

    @ApiModelProperty(value = "时效主键")
    private String operationTimeId;

    @ApiModelProperty(value = "时效编码")
    private String timeCode;

    @ApiModelProperty(value = "时效名称")
    private String timeName;

    @ApiModelProperty(value = "时效要求时长")
    private BigDecimal standardReqdTimeInProcess;

    @ApiModelProperty(value = "工艺ID")
    private String operationId;

    @ApiModelProperty(value = "工艺编码")
    private String operationName;

    @ApiModelProperty(value = "工艺名称")
    private String description;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "工位名称")
    private String workcellName;

    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    @ApiModelProperty(value = "事件Id")
    private String eventId;

    @ApiModelProperty(value = "事件人")
    private String eventBy;

    @ApiModelProperty(value = "事件人姓名")
    private String eventByName;

    @ApiModelProperty(value = "事件时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date eventTime;

    @ApiModelProperty(value = "事件类型")
    private String eventType;
}
