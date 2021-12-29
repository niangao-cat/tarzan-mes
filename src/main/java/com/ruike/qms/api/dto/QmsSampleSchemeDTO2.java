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
public class QmsSampleSchemeDTO2 {

    private static final long serialVersionUID = 7127811365905986959L;

    private String schemeId;
    @ApiModelProperty(value = "抽样计划类型")
    private String samplePlanType;
    @ApiModelProperty(value = "样本量字码")
    private String sampleSizeCodeLetter;
    @ApiModelProperty(value = "aql值")
    private String acceptanceQuantityLimit;
    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

}
