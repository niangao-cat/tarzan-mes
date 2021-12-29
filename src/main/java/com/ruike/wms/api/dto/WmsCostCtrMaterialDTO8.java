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
public class WmsCostCtrMaterialDTO8 {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "单据ID")
    private String instructionDocId;
    @ApiModelProperty(value = "单据行ID")
    private String instructionId;

    @ApiModelProperty(value = "已扫描的实物条码")
    private List<String> barCodes;

    private WmsCostCtrMaterialDTO2 docLine;
}
