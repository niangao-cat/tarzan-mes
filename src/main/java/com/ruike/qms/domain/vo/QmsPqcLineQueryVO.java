package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * QmsPqcLineQueryVO
 * @description: 巡检单查询行返回
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/06 10:20
 */
@Data
public class QmsPqcLineQueryVO implements Serializable {

    private static final long serialVersionUID = 3089977508205925602L;

    @ApiModelProperty(value = "序号")
    private BigDecimal number;

    @ApiModelProperty(value = "巡检单行Id")
    private String pqcLineId;

    @ApiModelProperty(value = " 检验项目")
    private String inspection;

    @ApiModelProperty(value = " 检验项目描述")
    private String inspectionDesc;

    @ApiModelProperty(value = " 工序Id")
    private String processId;

    @ApiModelProperty(value = " 检验方法")
    private String inspectionMethod;

    @ApiModelProperty(value = " 文本规格值")
    private String standardText;

    @ApiModelProperty(value = " 规格值从")
    private String standardFrom;

    @ApiModelProperty(value = " 规格值至")
    private String standardTo;

    @ApiModelProperty(value = " 检验标准")
    private String qcStandard;

    @ApiModelProperty(value = " 规格单位")
    private String standardUom;

    @ApiModelProperty(value = " 规格类型")
    @LovValue(value = "HME.TAG_VALUE_TYPE", meaningField = "standardTypeMeaning")
    private String standardType;

    @ApiModelProperty(value = "规格类型含义")
    private String standardTypeMeaning;

    @ApiModelProperty(value = " 检验工具")
    private String inspectionTool;

    @ApiModelProperty(value = "  检验结果")
    private String inspectionResult;

    @ApiModelProperty(value = " 工序")
    private String workcellName;

}
