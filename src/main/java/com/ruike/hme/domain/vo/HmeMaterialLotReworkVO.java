package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * SN返修信息
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/20 16:33
 */
@Data
public class HmeMaterialLotReworkVO {
    @ApiModelProperty(value = "物料批id")
    private String materialLotId;
    @ApiModelProperty(value = "物料批")
    private String materialLotCode;
    @ApiModelProperty(value = "返修标志")
    private String reworkFlag;
}
