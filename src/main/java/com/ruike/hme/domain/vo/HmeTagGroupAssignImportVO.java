package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据收集组与数据项关系VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/8/5 11:54
 */
@Data
public class HmeTagGroupAssignImportVO implements Serializable {

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "数据收集组")
    private String tagGroupId;

    @ApiModelProperty(value = "序号")
    private Double serialNumber;

    @ApiModelProperty(value = "数据项ID")
    private String tagId;

    @ApiModelProperty(value = "数据收集方式")
    private String collectionMethod;

    @ApiModelProperty(value = "允许缺失值")
    private String valueAllowMissing;

    @ApiModelProperty(value = "符合值")
    private String trueValue;

    @ApiModelProperty(value = "不符合值")
    private String falseValue;

    @ApiModelProperty(value = "最小值")
    private Double minimumValue;

    @ApiModelProperty(value = "最大值")
    private Double maximalValue;

    @ApiModelProperty(value = "计量单位")
    private String unit;

    @ApiModelProperty(value = "必需的数据条数")
    private Long mandatoryNum;

    @ApiModelProperty(value = "可选的数据条数")
    private Long optionalNum;

    @ApiModelProperty(value = "导入类型")
    private String importType;

    @ApiModelProperty(value = "数据收集组编码")
    private String tagGroupCode;

    @ApiModelProperty(value = "数据项编码")
    private String tagCode;
}
