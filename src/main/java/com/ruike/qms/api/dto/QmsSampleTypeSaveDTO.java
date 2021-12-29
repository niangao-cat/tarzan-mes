package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 抽样类型保存参数
 * @author: han.zhang
 * @create: 2020/05/07 10:32
 */
@Getter
@Setter
@ToString
public class QmsSampleTypeSaveDTO implements Serializable {
    private static final long serialVersionUID = 8198834722998548939L;

    @ApiModelProperty(value = "主键id")
    private String sampleTypeId;

    @ApiModelProperty(value = "抽样方式编码",required = true)
    private String sampleTypeCode;

    @ApiModelProperty(value = "抽样方式描述",required = true)
    private String sampleTypeDesc;

    @ApiModelProperty(value = "抽样类型",required = true)
    private String sampleType;

    @ApiModelProperty(value = "参数值")
    private Long parameters;

    @ApiModelProperty(value = "抽样标准",required = true)
    private String sampleStandard;

    @ApiModelProperty(value = "aql值",required = true)
    private String acceptanceQuantityLimit;

    @ApiModelProperty(value = "检验水平",required = true)
    private String inspectionLevels;

    @ApiModelProperty(value = "是否有效",required = true)
    private String enableFlag;

    @ApiModelProperty(value = "版本号")
    private Long objectVersionNumber;
}