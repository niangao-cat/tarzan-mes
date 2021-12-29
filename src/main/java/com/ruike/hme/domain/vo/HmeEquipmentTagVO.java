package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/8 15:21
 */
@Data
public class HmeEquipmentTagVO implements Serializable {

    private static final long serialVersionUID = -6405110026006534824L;

    @ApiModelProperty(value = "设备类别描述")
    private String equipmentCategoryDesc;
    @ApiModelProperty(value = "设备类别")
    private String equipmentCategory;
    @ApiModelProperty(value = "部门")
    private String businessId;
    @ApiModelProperty(value = "部门描述")
    private String businessName;
    @ApiModelProperty(value = "工艺编码")
    private String operationName;
    @ApiModelProperty(value = "工艺Id")
    private String operationId;
    @ApiModelProperty(value = "数据组编码")
    private String tagGroupCode;
    @ApiModelProperty(value = "数据组Id")
    private String tagGroupId;
    @ApiModelProperty(value = "设备管理类型")
    private String manageType;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "是否有效")
    private String enableFlag;
    @ApiModelProperty(value = "排序")
    private Long serialNumber;
    @ApiModelProperty(value = "项目编码")
    private String tagCode;
    @ApiModelProperty(value = "项目Id")
    private String tagId;
    @ApiModelProperty(value = "项目描述")
    private String tagDescriptions;
    @ApiModelProperty(value = "数据类型")
    private String valueType;
    @ApiModelProperty(value = "收集方式")
    private String collectionMethod;
    @ApiModelProperty(value = "周期")
    private String manageCycle;
    @ApiModelProperty(value = "精度")
    private BigDecimal accuracy;
    @ApiModelProperty(value = "最小值")
    private BigDecimal minimumValue;
    @ApiModelProperty(value = "标准值")
    private BigDecimal standardValue;
    @ApiModelProperty(value = "最大值")
    private BigDecimal maximalValue;
    @ApiModelProperty(value = "单位")
    private String uomName;
    @ApiModelProperty(value = "单位Id")
    private String uomId;
    @ApiModelProperty(value = "符合值")
    private String trueValue;
    @ApiModelProperty(value = "不符合值")
    private String falseValue;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "有效性")
    private String lineEnableFlag;
    @ApiModelProperty(value = "导入方式")
    private String importType;
    @ApiModelProperty(value = "站点")
    private String siteId;
}
