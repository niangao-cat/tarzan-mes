package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 检验报废-送货单
 *
 * @author jiangling.zheng@hand-china.com 2020-08-26 14:58
 */
@Data
public class QmsCheckScrapDocLineDTO2 {
    private static final long serialVersionUID = 4942253858816696526L;

    @ApiModelProperty("单据ID")
    private String instructionDocId;
    @ApiModelProperty("指令ID")
    private String instructionId;

    List<QmsCheckScrapBarCodeDTO2> barCodeList;
}
