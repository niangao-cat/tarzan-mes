package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 检验报废-条码
 *
 * @author jiangling.zheng@hand-china.com 2020-08-26 14:58
 */
@Data
public class QmsCheckScrapBarCodeDTO2 {
    private static final long serialVersionUID = 4942253858816696526L;

    @ApiModelProperty("指令实绩明细ID")
    private String actualDetailId;
    @ApiModelProperty("单据ID")
    private String materialLotId;
    @ApiModelProperty("单据编码")
    private String materialLotCode;
    @ApiModelProperty("报废数量")
    private BigDecimal scrapQty;
}
