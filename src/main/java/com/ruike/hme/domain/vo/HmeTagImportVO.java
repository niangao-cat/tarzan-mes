package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据收集项-导入
 *
 * @author sanfeng.zhang@hand-china.com 2020/8/4 14:09
 */
@Data
public class HmeTagImportVO implements Serializable {

    private static final long serialVersionUID = -6232979272247055187L;


    private Long tenantId;

    @ApiModelProperty(value = "数据项主键")
    private String tagId;

    @ApiModelProperty(value = "数据项编码")
    private String tagCode;

    @ApiModelProperty(value = "数据项描述")
    private String tagDescription;

    @ApiModelProperty(value = "数据收集方式")
    private String collectionMethod;

    @ApiModelProperty(value = "数据类型")
    private String valueType;

    @ApiModelProperty(value = "最小值")
    private Double minimumValue;

    @ApiModelProperty(value = "最大值")
    private Double maximalValue;

    @ApiModelProperty(value = "计量单位")
    private String unit;

    @ApiModelProperty(value = "符合值")
    private String trueValue;

    @ApiModelProperty(value = "不符合值")
    private String falseValue;

    @ApiModelProperty(value = "必需的数据条数")
    private Long mandatoryNum;

    @ApiModelProperty(value = "可选的数据条数")
    private Long optionalNum;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否启用")
    private String enableFlag;

    @ApiModelProperty(value = "扩展字段名")
    private String attrName;

    @ApiModelProperty(value = "扩展字段值")
    private String attrValue;

    @ApiModelProperty(value = "导入方式")
    private String importMethod;

    @ApiModelProperty(value = "导入类型")
    private String importType;

    @ApiModelProperty(value = "过程数据标识")
    private String processFlag;
}
