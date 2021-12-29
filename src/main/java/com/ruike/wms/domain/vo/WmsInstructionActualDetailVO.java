package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;

/**
 * 单据实际
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 14:37
 */
@Data
public class WmsInstructionActualDetailVO {
    @ApiModelProperty("单据行ID")
    private String instructionId;
    @ApiModelProperty("单据实际行ID")
    private String actualId;
    @ApiModelProperty("单据实际行明细ID")
    private String actualDetailId;
    @ApiModelProperty("指令行状态")
    private String instructionStatus;
    @ApiModelProperty("工段")
    private String workcellCode;
    @ApiModelProperty("物料批ID")
    private String materialLotId;
    @ApiModelProperty("物料批编码")
    private String materialLotCode;
    @ApiModelProperty("容器ID")
    private String containerId;
    @ApiModelProperty("容器编码")
    private String containerCode;
    @ApiModelProperty("货位ID")
    private String locatorId;
    @ApiModelProperty("货位编码")
    private String locatorCode;
    @ApiModelProperty("物料批状态")
    @LovValue(lovCode = "WMS.MTLOT.STATUS", meaningField = "materialLotStatusMeaning")
    private String materialLotStatus;
    @ApiModelProperty("物料批状态含义")
    private String materialLotStatusMeaning;
    @ApiModelProperty(value = "批次")
    private String lot;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "单位")
    private String primaryUomId;
    @ApiModelProperty(value = "单位编码")
    private String primaryUomCode;
    @ApiModelProperty(value = "数量")
    private BigDecimal primaryUomQty;
    @ApiModelProperty(value = "质量状态")
    @LovValue(lovCode = "WMS.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;
    @ApiModelProperty(value = "质量状态")
    private String qualityStatusMeaning;
    @ApiModelProperty(value = "销售订单号")
    private String soNum;
    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;
    @ApiModelProperty(value = "实际数量")
    private BigDecimal actualQty;
}
