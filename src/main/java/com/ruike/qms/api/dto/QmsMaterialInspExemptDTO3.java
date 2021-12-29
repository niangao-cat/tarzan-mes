package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-04-26 12:59
 */
@Data
public class QmsMaterialInspExemptDTO3 {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "免检ID")
    private String exemptionId;
}
