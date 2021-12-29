package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-04-15 17:19
 */
@Data
public class WmsInvTransferDTO3 {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "实物条码")
    private String barCode;
    @ApiModelProperty(value = "单据ID")
    private String instructionDocId;
    @ApiModelProperty(value = "单据类型")
    private String instructionDocType;
    @ApiModelProperty(value = "货位编码")
    private String locatorCode;

    private List<WmsInvTransferDTO2> docLineList;
    // modify by jiangling.zheng 不需要此参数 20200821
    // add by wsg
//    private List<WmsCostCtrMaterialDTO3> barCodeList;
}
