package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 检验报废-条码扫描参数
 *
 * @author jiangling.zheng@hand-china.com 2020-08-26 14:58
 */
@Data
public class QmsCheckScrapParamsDTO {
    private static final long serialVersionUID = 4942253858816696526L;

    @ApiModelProperty("扫描条码")
    private String barCode;
    @ApiModelProperty("单据编码")
    private String instructionDocNum;
    @ApiModelProperty("指令编码")
    private String instructionNum;
}
