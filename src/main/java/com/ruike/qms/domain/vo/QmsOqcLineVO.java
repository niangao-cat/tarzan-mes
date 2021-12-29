package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.*;
import java.math.*;
import java.util.*;

/**
 * @Classname QmsOqcLineVO
 * @Description 检验单行视图
 * @Date 2020/8/28 15:25
 * @Author yuchao.wang
 */
@Data
public class QmsOqcLineVO implements Serializable {
    private static final long serialVersionUID = -4883941667721627139L;
    
    @ApiModelProperty(value = "检验单头表ID")
    private String oqcHeaderId;
    
    @ApiModelProperty(value = "检验单行主键ID")
    private String oqcLineId;
    
    @ApiModelProperty(value = "检验项序号")
    private BigDecimal number;

    @LovValue(lovCode = "QMS.PQC_INSPECTION_LINE_TYPE", meaningField="inspectionTypeMeaning")
    @ApiModelProperty(value = "检验项类别")
    private String inspectionType;

    @ApiModelProperty(value = "检验项类别描述")
    private String inspectionTypeMeaning;

    @ApiModelProperty(value = "检验项目")
    private String inspection;

    @ApiModelProperty(value = "检验项描述")
    private String inspectionDesc;

    @LovValue(lovCode = "QMS.PQC_STANDARD_TYPE", meaningField="standardTypeMeaning")
    @ApiModelProperty(value = "规格类型")
    private String standardType;

    @ApiModelProperty(value = "规格类型描述")
    private String standardTypeMeaning;

    @ApiModelProperty(value = "文本规格值")
    private String standardText;

    @ApiModelProperty(value = "规格值从")
    private BigDecimal standardFrom;

    @ApiModelProperty(value = "规格值至")
    private BigDecimal standardTo;

    @ApiModelProperty(value = "精度")
    private BigDecimal accuracy;

    @ApiModelProperty(value = "规格单位")
    private String standardUom;

    @LovValue(lovCode = "QMS.PQC_INSPECTION_TOOL", meaningField="inspectionToolMeaning")
    @ApiModelProperty(value = "检验工具")
    private String inspectionTool;

    @ApiModelProperty(value = "检验工具描述")
    private String inspectionToolMeaning;

    @LovValue(lovCode = "QMS.PQC_INSPECTION_RESULT", meaningField="inspectionResultMeaning")
    @ApiModelProperty(value = "结论")
    private String inspectionResult;

    @ApiModelProperty(value = "结论描述")
    private String inspectionResultMeaning;

    @ApiModelProperty(value = "附件ID")
    private String attachmentUuid;

    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    @ApiModelProperty(value = "检验单明细信息")
    private List<QmsOqcDetailsVO> detailList;
}