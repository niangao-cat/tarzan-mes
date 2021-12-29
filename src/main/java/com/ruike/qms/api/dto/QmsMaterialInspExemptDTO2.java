package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-04-26 12:59
 */
@Data
public class QmsMaterialInspExemptDTO2 {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "工厂ID")
    private String siteId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
}
