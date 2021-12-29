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
public class WmsInvTransferDTO5 {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "单据ID")
    private String instructionDocId;
    @ApiModelProperty(value = "单据ID")
    private String instructionDocNum;
    @ApiModelProperty(value = "单据类型")
    private String instructionDocType;

    private List<WmsInvTransferDTO2> docLineList;
    @ApiModelProperty(value = "已扫描的物料批 ")
    private List<WmsCostCtrMaterialDTO3> materialLotList;
}
