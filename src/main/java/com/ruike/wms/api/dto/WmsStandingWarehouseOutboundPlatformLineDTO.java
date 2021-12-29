package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.List;

@Data
public class WmsStandingWarehouseOutboundPlatformLineDTO implements Serializable {
    private static final long serialVersionUID = 7411985887440836360L;
    @ApiModelProperty(value = "条码Id")
    private String materialLotId;
    @ApiModelProperty(value = "条码")
    private String materialLotCode;
    @ApiModelProperty(value = "物料Id")
    private String materialId;
    @ApiModelProperty(value = "单据行Id")
    private String instructionId;
    @ApiModelProperty(value = "单据行状态")
    private String instructionStatus;
    @ApiModelProperty(value = "站点Id")
    private String siteId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "销售订单")
    private String materialLotSoNumSoLineNum;
    @ApiModelProperty(value = "仓库ID")
    private String topLlocatorId;
    @ApiModelProperty(value = "仓库")
    private String topLlocatorCode;
    @ApiModelProperty(value = "货位")
    private String locatorCode;
    @ApiModelProperty(value = "数量")
    private Double primaryUomQty;
    @ApiModelProperty(value = "当前容器")
    private String containerId;
    @ApiModelProperty(value = "当前容器编码")
    private String containerCode;
    @ApiModelProperty(value = "当前容器名称")
    private String containerName;
    @ApiModelProperty(value = "有效性")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "enableFlagMeaing")
    private String enableFlag;
    @ApiModelProperty(value = "有效性含义")
    private String enableFlagMeaing;

    @ApiModelProperty(value = "主键Id")
    private String keyId;

    @ApiModelProperty(value = "状态")
    @LovValue(lovCode = "MT.MTLOT.STATUS", meaningField = "statusMeaing")
    private String status;
    @ApiModelProperty(value = "有效性含义")
    private String statusMeaing;


}
