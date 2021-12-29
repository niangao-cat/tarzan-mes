package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
@Data
public class WmsStandingWarehouseOutboundPlatformHeadDTO implements Serializable {
    private static final long serialVersionUID = -2553709119127991297L;
    @ApiModelProperty(value = "租户Id")
    private Long tenantId;
    @ApiModelProperty(value = "单据编号")
    private String instructionDocNum;
    @ApiModelProperty(value = "单据Id")
    private String  instructionDocId;
    @ApiModelProperty(value = "单据类型")
    @LovValue(lovCode = "WX.WMS.WCS_TASK_DOC_TYPE_LIMIT", meaningField = "instructionDocTypeMeaning")
    private String instructionDocType;
    @ApiModelProperty(value = "单据类型含义")
    private String instructionDocTypeMeaning;
    @ApiModelProperty(value = "单据状态")
    @LovValue(lovCode = "WX.WMS.WCS_TASK_DOC_STATUS_LIMIT", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;
    @ApiModelProperty(value = "单据状态含义")
    private String instructionDocStatusMeaning;
}
