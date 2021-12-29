package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;

/**
 * <p>
 * 成品发货明细
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/15 17:11
 */
@Data
public class WmsProductPrepareDetailVO {
    @ApiModelProperty("物料批Id")
    private String materialLotId;
    @ApiModelProperty("物料批编码")
    private String materialLotCode;
    @ApiModelProperty("容器Id")
    private String containerId;
    @ApiModelProperty("容器编码")
    private String containerCode;
    @ApiModelProperty("物料编码")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("单位Id")
    private String uomId;
    @ApiModelProperty("单位")
    private String uomCode;
    @ApiModelProperty("批次")
    private String lot;
    @ApiModelProperty("物料批状态")
    @LovValue(lovCode = "WMS.MTLOT.STATUS", meaningField = "materialLotStatusMeaning")
    private String materialLotStatus;
    @ApiModelProperty("物料批状态含义")
    private String materialLotStatusMeaning;
    @ApiModelProperty("明细数量")
    private BigDecimal actualQty;
    @ApiModelProperty("货位Id")
    private String locatorId;
    @ApiModelProperty("货位编码")
    private String locatorCode;
    @ApiModelProperty("销售订单")
    private String soNum;
    @ApiModelProperty("销售订单行")
    private String soLineNum;
}
