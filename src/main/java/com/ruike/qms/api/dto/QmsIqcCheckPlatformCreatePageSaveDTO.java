package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description 新建页面保存时传入DTO
 * @Author tong.li
 * @Date 2020/5/14 17:22
 * @Version 1.0
 */
@Data
public class QmsIqcCheckPlatformCreatePageSaveDTO implements Serializable {
    private static final long serialVersionUID = -3626282769597916804L;

    @ApiModelProperty(value = "质检单头ID")
    private String 	iqcHeaderId;
    @ApiModelProperty(value = "质检单行ID")
    private String 	iqcLineId;
    @ApiModelProperty(value = "检验类别")
    private String inspectionType;
    @ApiModelProperty(value = "检验项目")
    private String 	inspection;
    @ApiModelProperty(value = "检验项目描述")
    private String inspectionDesc;
    @ApiModelProperty(value = "抽样类型")
    private String sampleTypeCode;
    @ApiModelProperty(value = "抽样id")
    private String sampleTypeId;
    @ApiModelProperty(value = "检验水平")
    private String inspectionLevels;
    @ApiModelProperty(value = "缺陷等级")
    private String defectLevels;
    @ApiModelProperty(value = "AQL")
    private String acceptanceQuantityLimit;
    @ApiModelProperty(value = "抽样数量")
    private BigDecimal sampleSize;
    @ApiModelProperty(value = "AC")
    private BigDecimal AC;
    @ApiModelProperty(value = "RE")
    private BigDecimal RE;
    @ApiModelProperty(value = "文本规格值")
    private String standardText;
    @ApiModelProperty(value = "规格值从")
    private BigDecimal standardFrom;
    @ApiModelProperty(value = "规格值至")
    private BigDecimal standardTo;
    @ApiModelProperty(value = "规格单位ID")
    private String uomId;
    @ApiModelProperty(value = "检验工具")
    private String inspectionTool;

    @ApiModelProperty(value = "序号")
    private String 	number;

    @ApiModelProperty(value = "规格类型")
    private String standardType;

}
