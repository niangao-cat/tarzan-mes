package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-04-15 17:19
 */
@Data
public class WmsInvTransferDTO6 {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "单据ID")
    private String instructionDocId;
    @ApiModelProperty(value = "单据类型")
    private String instructionDocType;

    @ApiModelProperty(value = "已扫描的实物条码")
    private List<String> barCodes;

    private WmsInvTransferDTO2 docLine;
}
