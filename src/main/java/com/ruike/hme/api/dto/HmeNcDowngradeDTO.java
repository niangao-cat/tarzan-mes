package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeNcDowngradeDTO
 *
 * @author: chaonan.hu@hand-china.com 2021-05-18 11:35:47
 **/
@Data
public class HmeNcDowngradeDTO implements Serializable {
    private static final long serialVersionUID = 1471587719433035007L;

    @ApiModelProperty(value = "主键ID,传则更新，不传则新增")
    private String downgradeId;

    @ApiModelProperty(value = "物料ID",required = true)
    private String materialId;

    @ApiModelProperty(value = "不良代码ID",required = true)
    private String ncCodeId;

    @ApiModelProperty(value = "降级物料ID",required = true)
    private String transitionMaterialId;

    @ApiModelProperty(value = "有效性",required = true)
    private String enableFlag;
}
