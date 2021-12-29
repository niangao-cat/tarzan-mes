package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.List;

@Data
public class WmsStandingWarehouseOutboundPlatformDTO implements Serializable {
    private static final long serialVersionUID = -4905274715060659504L;
    @ApiModelProperty(value = "租户Id")
    private Long tenantId;
    @ApiModelProperty(value = "单据编号")
    private String instructionDocNum;
    @ApiModelProperty(value = "单据Id")
    private String instructionDocId;
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


    @ApiModelProperty(value = "单据行Id")
    private String instructionId;
    @ApiModelProperty(value = "指令状态")
    @LovValue(lovCode = "WX.WMS.INSTRUCTION_STATUS", meaningField = "instructionStatusMeaning")
    private String instructionStatus;
    @ApiModelProperty(value = "指令状态含义")
    private String instructionStatusMeaning;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "站点I的")
    private String siteId;
    @ApiModelProperty(value = "制单数量")
    private Double quantity;
    @ApiModelProperty(value = "来源仓库")
    private String fromLocatorId;
    @ApiModelProperty(value = "实绩数量")
    private Double actualQty;
    @ApiModelProperty(value = "行号")
    private String instructionLineNum;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "仓库编码")
    private String locatorCode;
    @ApiModelProperty(value = "仓库名称")
    private String locatorName;
    @ApiModelProperty(value = "销售订单")
    private String soNumSoLineNum;
/*    @ApiModelProperty(value = "立库现有量")
    private Double materialLotCodeQuantity;*/
    @ApiModelProperty(value = "出口号")
    private String exitNum;
    @ApiModelProperty(value = "soNum;")
    private String soNum;
    @ApiModelProperty(value = "soLineNum")
    private String soLineNum;
    @ApiModelProperty(value = "立库现有量Flag")
    private String specStockFlag;
    @ApiModelProperty(value = "立库现有量")
    private Long stangingQuantity;

    @ApiModelProperty(value = "SN指定")
    private List<WmsStandingWarehouseOutboundPlatformLineDTO> lineDTOList;

    @ApiModelProperty(value = "批量SN条码")
    private List<String> materialLotCodesList;

    @ApiModelProperty(value = "SN扩展字段;")
    private String sn;
/*    @ApiModelProperty(value = "单个SN条码")
    private String materialLotCode;*/

}
