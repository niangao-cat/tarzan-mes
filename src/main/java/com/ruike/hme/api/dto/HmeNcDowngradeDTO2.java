package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeNcDowngradeDTO2
 *
 * @author: chaonan.hu@hand-china.com 2021-05-18 14:35:21
 **/
@Data
public class HmeNcDowngradeDTO2 implements Serializable {
    private static final long serialVersionUID = 1471587719433035007L;

    @ApiModelProperty(value = "产品编码ID")
    private String materialId;

    @ApiModelProperty(value = "不良代码ID")
    private String ncCodeId;

    @ApiModelProperty(value = "降级物料ID")
    private String transitionMaterialId;

    @ApiModelProperty(value = "有效性")
    private String enableFlag;
}
