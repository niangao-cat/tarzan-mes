package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-04-26 12:59
 */
@Data
public class QmsSampleSchemeDTO {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "主键")
    private String schemeId;
    @ApiModelProperty(value = "抽样计划类型",required = true)
    @LovValue(value = "QMS.IQC_SAMPLE_PLAN_TYPE", meaningField = "samplePlanTypeMeaning")
    private String samplePlanType;
    @ApiModelProperty(value = "抽样标准类型说明")
    private String samplePlanTypeMeaning;
    @ApiModelProperty(value = "抽样标准类型",required = true)
    @LovValue(value = "QMS.IQC_SAMPLE_STANDARD_TYPE", meaningField = "sampleStandardTypeMeaning")
    private String sampleStandardType;
    @ApiModelProperty(value = "抽样标准类型说明")
    private String sampleStandardTypeMeaning;
    @ApiModelProperty(value = "样本量字码")
    @LovValue(value = "QMS.IQC_SAMPLE_SIZE_CODE_LEVEL", meaningField = "sampleSizeCodeLetterMeaning")
    private String sampleSizeCodeLetter;
    @ApiModelProperty(value = "样本量字码说明")
    private String sampleSizeCodeLetterMeaning;
    @ApiModelProperty(value = "批量上限（零抽样方案用,非负整数）")
    private Long lotUpperLimit;
    @ApiModelProperty(value = "批量下限（零抽样方案用,非负整数）")
    private Long lotLowerLimit;
    @ApiModelProperty(value = "aql值",required = true)
    @LovValue(value = "QMS.IQC_AQL", meaningField = "acceptanceQuantityLimitMeaning")
    private String acceptanceQuantityLimit;
    @ApiModelProperty(value = "aql值说明")
    private String acceptanceQuantityLimitMeaning;
    @ApiModelProperty(value = "抽样数量（非负整数，除-1，-1代表全检）")
    private Long sampleSize;
    @ApiModelProperty(value = "ac值（非负整数）")
    private Long ac;
    @ApiModelProperty(value = "re值（非负整数）")
    private Long re;
    @ApiModelProperty(value = "是否有效",required = true)
    @LovValue(value = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;
    @ApiModelProperty(value = "有效标志描述")
    private String enableFlagMeaning;
    @ApiModelProperty(value = "扩展字段1")
    private String attribute1;
    @ApiModelProperty(value = "扩展字段1含义")
    private String attribute1Meaning;
}
