package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WmsProductionRequisitionMaterialExecutionDetailDTO implements Serializable {
    private static final long serialVersionUID = -649612894763919231L;
    @ApiModelProperty(value = "实物条码Id")
    private String materialLotId;
    @ApiModelProperty(value = "实物条码")
    private String materialLotCode;
    @ApiModelProperty(value = "物料Id")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "条码数量")
    private Double primaryUomQty;
    @ApiModelProperty(value = "单位Id")
    private String uomId;
    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "单位描述")
    private String uomName;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "批次")
    private String lot;
    @ApiModelProperty(value = "站点")
    private String siteId;
    @ApiModelProperty(value = "父货位")
    private String parentLocatorId;
    @ApiModelProperty(value = "父货位")
    private String materialLotLocatorId;
    @ApiModelProperty(value = "货位")
    private String locatorId;
    @ApiModelProperty(value = "货位编码")
    private String locatorCode;
    @ApiModelProperty(value = "货位描述")
    private String locatorName;
    @ApiModelProperty(value = "质量状态")
    @LovValue(value = "WMS.MTLOT.QUALITY_STATUS", meaningField = "quantityStatusMeaning")
    private String quantityStatus;
    @ApiModelProperty(value = "质量状态含义")
    private String quantityStatusMeaning;
    @ApiModelProperty(value = "有效性")
    private String enableFlag;
    @ApiModelProperty(value = "当前容器")
    private String currentContainerCode;
    @ApiModelProperty(value = "当前容器")
    private String currentContainerId;
    @ApiModelProperty(value = "冻结标识")
    private String freezeFlag;
    @ApiModelProperty(value = "盘点标识")
    private String stocktakeFlag;
    @ApiModelProperty(value = "在制品")
    private String mfFlag;
    @ApiModelProperty(value = "销售订单")
    private String soNum;
    @ApiModelProperty(value = "行号")
    private String soLineNum;
    @ApiModelProperty(value = "实绩Id")
    private String actualId;
    @ApiModelProperty(value = "实绩数量")
    private Double actualQty;
    @ApiModelProperty(value = "行状态")
    private String instructionStatus;
    @ApiModelProperty(value = "单据Id")
    private String instructionId;
    @ApiModelProperty(value = "单据状态")
    private String instructionType;
    @ApiModelProperty(value = "单据行")
    private String instructionNum;
    @ApiModelProperty(value = "条码状态")
    private String status;
    @ApiModelProperty(value = "容器Id")
    private String contaierId;
    @ApiModelProperty(value = "容器编码")
    private String containerCode;
    @ApiModelProperty(value = "容器描述")
    private String containerName;
    @ApiModelProperty(value = "上层容器")
    private String topContainerId;
    @ApiModelProperty(value = "条码货位编码")
    private String materialLotLocaCode;
    @ApiModelProperty(value = "条码货位描述")
    private String materialLotLocaName;
    @ApiModelProperty(value = "条码货位Id")
    private String materialLotLocaId;


}
