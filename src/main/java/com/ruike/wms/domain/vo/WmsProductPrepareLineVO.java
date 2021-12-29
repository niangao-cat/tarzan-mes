package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;

/**
 * <p>
 * 成品备货行
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 10:21
 */
@Data
public class WmsProductPrepareLineVO {
    @ApiModelProperty("行ID")
    private String instructionId;
    @ApiModelProperty("行号")
    private Integer instructionLineNum;
    @ApiModelProperty("状态")
    @LovValue(lovCode = "WX.WMS.SO_DELIVERY_LINE_STATUS", meaningField = "instructionStatusMeaning")
    private String instructionStatus;
    @ApiModelProperty("状态含义")
    private String instructionStatusMeaning;
    @ApiModelProperty("物料Id")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("需求数")
    private BigDecimal demandQty;
    @ApiModelProperty("历史执行数量（实发数）")
    private BigDecimal actualQty;
    @ApiModelProperty("单位Id")
    private String uomId;
    @ApiModelProperty("单位")
    private String uomCode;
    @ApiModelProperty("发货工厂Id")
    private String fromSiteId;
    @ApiModelProperty("发货工厂")
    private String fromSiteCode;
    @ApiModelProperty("发货仓库Id")
    private String fromLocatorId;
    @ApiModelProperty("发货仓库")
    private String fromLocatorCode;
    @ApiModelProperty("销售订单")
    private String soNum;
    @ApiModelProperty("销售订单行号")
    private String specStockFlag;
    @ApiModelProperty("销售订单行")
    private String soLineNum;
    @ApiModelProperty("按单标志")
    private String soFlag;
    @ApiModelProperty("允差上限")
    private BigDecimal toleranceUpperLimit;
    @ApiModelProperty("条码个数")
    private Integer materialLotCount;
    @ApiModelProperty("取货货位ID")
    private String pickUpLocatorId;
    @ApiModelProperty("取货货位编码")
    private String pickUpLocatorCode;
    @ApiModelProperty("解绑标识")
    private String unBundingFlag;
    @ApiModelProperty("行扩展字段SN")
    private String sn;

}
