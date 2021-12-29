package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 物料存储属性导入模板
 *
 * @author yapeng.yao@hand-china.com 2020/09/10 11:33
 */
@Data
public class QmsImportMaterialInspVO implements Serializable {

    private static final long serialVersionUID = 1191967187892284906L;

    // qms_material_insp_scheme
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

    // qms_material_tag_group_rel
    @ApiModelProperty(value = "检验组ID")
    private String tagGroupId;

    @ApiModelProperty(value = "检验组编码")
    private String tagGroupCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    // qms_material_insp_content
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

    @ApiModelProperty(value = "缺陷等级")
    private String defectLevel;

    @ApiModelProperty(value = "文本规格值")
    private String standardText;

    @ApiModelProperty(value = "检验工具")
    private String inspectionTool;

    @ApiModelProperty(value = "检验方法")
    private String inspectionMethod;

    @ApiModelProperty(value = "抽样类型")
    private String sampleType;

    @ApiModelProperty(value = "备注")
    private String contentRemark;
}
