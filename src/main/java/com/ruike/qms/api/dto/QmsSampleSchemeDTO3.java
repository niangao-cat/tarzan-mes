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
public class QmsSampleSchemeDTO3 {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "主键")
    private String schemeId;
    @ApiModelProperty(value = "抽样计划类型",required = true)
    private String samplePlanType;
    @ApiModelProperty(value = "抽样标准类型",required = true)
    private String sampleStandardType;
    @ApiModelProperty(value = "样本量字码")
    private String sampleSizeCodeLetter;
    @ApiModelProperty(value = "批量上限（零抽样方案用,非负整数）")
    private Long lotUpperLimit;
    @ApiModelProperty(value = "批量下限（零抽样方案用,非负整数）")
    private Long lotLowerLimit;
    @ApiModelProperty(value = "aql值",required = true)
    private String acceptanceQuantityLimit;
    @ApiModelProperty(value = "抽样数量（非负整数，除-1，-1代表全检）")
    private Long sampleSize;
    @ApiModelProperty(value = "ac值（非负整数）")
    private Long ac;
    @ApiModelProperty(value = "re值（非负整数）")
    private Long re;
    @ApiModelProperty(value = "是否有效",required = true)
    private String enableFlag;

    @ApiModelProperty(value = "抽样方案lov值")
    private String attribute1;
}
