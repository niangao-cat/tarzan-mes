package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description IQC检验平台  新建界面检验组LOV选择后带出信息
 * @Author tong.li
 * @Date 2020/5/14 11:29
 * @Version 1.0
 */
@Data
public class QmsIqcCheckPlatformCreateBringDTO implements Serializable {
    private static final long serialVersionUID = 3264764709107313689L;

    @ApiModelProperty(value = "检验项目")
    private String 	inspection;
    @ApiModelProperty(value = "检验项目描述")
    private String inspectionDesc;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "检验方法")
    private String collectionMethod;
    @ApiModelProperty(value = "检验方法含义")
    private String collectionMethodMeaning;
    @ApiModelProperty(value = "规格类型")
    private String standardType;
    @ApiModelProperty(value = "规格类型含义")
    private String valueTypeMeaning;
    @ApiModelProperty(value = "规格值从")
    private BigDecimal standardFrom;
    @ApiModelProperty(value = "规格值至")
    private BigDecimal standardTo;
    @ApiModelProperty(value = "排序码")
    private String orderKey;
    @LovValue(value = "QMS.INSPECTION_CONTENT_TYPE", meaningField = "inspectionTypeMeaning")
    @ApiModelProperty(value = "检验类别")
    private String inspectionType;
    @ApiModelProperty(value = "检验类别含义")
    private String inspectionTypeMeaning;
    @ApiModelProperty(value = "精度")
    private String accuracy;
    @ApiModelProperty(value = "文本规格值")
    private String standardText;
    @ApiModelProperty(value = "检验工具")
    private String inspectionTool;
    @ApiModelProperty(value = "检验水平")
    private String inspectionLevels;
    @ApiModelProperty(value = "规格单位ID")
    private String uomId;
    @ApiModelProperty(value = "规格单位编码")
    private String uomCode;
    @ApiModelProperty(value = "规格单位名称")
    private String uomName;

    @ApiModelProperty(value = "主键")
    private String tagGroupAssignId;

    @ApiModelProperty(value = "规格类型含义")
    private String standardTypeMeaning;
}
