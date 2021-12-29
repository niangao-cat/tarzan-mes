package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 巡检检验计划导入模板
 *
 * @author yapeng.yao@hand-china.com 2020/09/10 11:33
 */
@Data
public class QmsImportPqcMaterialInspSchemeVO implements Serializable {

    private static final long serialVersionUID = 5560263614995526669L;

    // qms_pqc_inspection_scheme
    @ApiModelProperty(value = "组织ID")
    private String siteId;

    @ApiModelProperty(value = "组织编码")
    private String siteCode;

    @ApiModelProperty(value = "物料类别ID")
    private String materialCategoryId;

    @ApiModelProperty(value = "物料类别编码")
    private String categoryCode;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "检验类型")
    private String inspectionType;

    @ApiModelProperty(value = "检验文件号")
    private String inspectionFile;

    @ApiModelProperty(value = "文件版本号")
    private String fileVersion;

    // qms_pqc_group_rel
    @ApiModelProperty(value = "检验组ID")
    private String tagGroupId;

    @ApiModelProperty(value = "检验组编码")
    private String tagGroupCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    // qms_pqc_inspection_content
    @ApiModelProperty(value = "检验项目ID")
    private String tagId;

    @ApiModelProperty(value = "检验项目")
    private String tagCode;

    @ApiModelProperty(value = "检验项目描述")
    private String tagDesc;

    @ApiModelProperty(value = "检验项类别")
    private String contentInspectionType;

    @ApiModelProperty(value = "orderKey")
    private String orderKey;

    @ApiModelProperty(value = "工序ID")
    private String processId;

    @ApiModelProperty(value = "工序编码")
    private String processCode;

    @ApiModelProperty(value = "频率")
    private String frequency;

    @ApiModelProperty(value = "规格类型")
    private String standardType;

    @ApiModelProperty(value = "精度")
    private BigDecimal accuracy;

    @ApiModelProperty(value = "规格值从")
    private BigDecimal standardFrom;

    @ApiModelProperty(value = "规格值至")
    private BigDecimal standardTo;

    @ApiModelProperty(value = "规格单位")
    private String standardUom;

    @ApiModelProperty(value = "文本规格值")
    private String standardText;

    @ApiModelProperty(value = "检验工具")
    private String inspectionTool;

    @ApiModelProperty(value = "备注")
    private String contentRemark;

    @ApiModelProperty(value = "标识(新增&更新)")
    private String optFlag;
}
