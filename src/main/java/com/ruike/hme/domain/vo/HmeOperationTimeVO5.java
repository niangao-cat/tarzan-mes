package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeOperationTimeVO5
 * @author: chaonan.hu@hand-china.com 2020-08-12 14:19:57
 **/
@Data
public class HmeOperationTimeVO5 implements Serializable {
    private static final long serialVersionUID = 1836285392197270660L;

    @ApiModelProperty(value = "关联物料主键")
    private String operationTimeMaterialId;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "生产版本Id")
    private String productionVersionId;

    @ApiModelProperty(value = "生产版本名称")
    private String productionVersion;

    @ApiModelProperty(value = "版本描述")
    private String description;

    @ApiModelProperty(value = "时效要求时长")
    private BigDecimal standardReqdTimeInProcess;

    @ApiModelProperty(value = "是否启用")
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
