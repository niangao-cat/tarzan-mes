package com.ruike.wms.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

import org.hzero.boot.platform.lov.annotation.LovValue;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * WmsMaterialPostingVO2
 *
 * @author liyuan.lv@hand-china.com 2020/06/13 11:57
 */
@Data
public class WmsMaterialLotLineVO implements Serializable {

    private static final long serialVersionUID = 96058076203269675L;

    @ApiModelProperty("单据ID")
    private String instructionDocId;
    @ApiModelProperty("单据Num")
    private String instructionDocNum;
    @ApiModelProperty("单据类型")
    private String instructionDocType;
    @ApiModelProperty("单据状态")
    @LovValue(value = "WMS.DELIVERY_DOC.STATUS",meaningField ="instructionDocStatusMeaning" )
    private String instructionDocStatus;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("指令ID")
    private String instructionId;
    @ApiModelProperty("指令Num")
    private String instructionNum;
    @ApiModelProperty("指令类型")
    private String instructionType;
    @ApiModelProperty("指令状态")
    @LovValue(value = "WMS.DELIVERY_DOC_LINE.STATUS",meaningField ="instructionStatusMeaning" )
    private String instructionStatus;
    @ApiModelProperty("供应商id")
    private String supplierId;
    @ApiModelProperty("供应商code")
    private String supplierCode;
    @ApiModelProperty("供应商名称")
    private String supplierName;
    @ApiModelProperty("条码ID")
    private String materialLotId;
    @ApiModelProperty("条码")
    private String materialLotCode;
    @ApiModelProperty("工厂id")
    private String siteId;
    @ApiModelProperty("工厂code")
    private String siteCode;
    @ApiModelProperty("工厂名称")
    private String siteName;
    @ApiModelProperty("条码数量")
    private BigDecimal primaryUomQty;
    @ApiModelProperty("单位ID")
    private String uomId;
    @ApiModelProperty("单位code")
    private String uomCode;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料code")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("条码货位ID")
    private String materialLotLocatorId;
    @ApiModelProperty("条码货位code")
    private String materialLotLocatorCode;
    @ApiModelProperty("条码货位名称")
    private String materialLotLocatorName;
    @ApiModelProperty("仓库id")
    private String warehouseId;
    @ApiModelProperty("仓库编码")
    private String warehouseCode;
    @ApiModelProperty("目标货位ID")
    private String transferLocatorId;
    @ApiModelProperty("目标货位code")
    private String transferLocatorCode;
    @ApiModelProperty("是否启用")
    @LovValue(value = "WMS.FLAG_YN",meaningField ="enableFlagMeaning" )
    private String enableFlag;
    @ApiModelProperty("是否启用Meaning")
    private String enableFlagMeaning;
    @LovValue(value = "WMS.MTLOT.STATUS",meaningField ="materialLotStatusMeaning" )
    private String materialLotStatus;
    @ApiModelProperty("条码状态Meaning")
    private String materialLotStatusMeaning;

    @ApiModelProperty("批次")
    private String lot;
    @ApiModelProperty("当前容器Id")
    private String currentContainerId;

    @ApiModelProperty("实绩货位Id")
    private String actualLocatorId;

    @ApiModelProperty("检验报废数量")
    private BigDecimal inspectScrapQty = BigDecimal.valueOf(0);
    @ApiModelProperty("已入库上架数量")
    private BigDecimal stockInQty = BigDecimal.valueOf(0);
    @ApiModelProperty("实际接收数量")
    private BigDecimal actualReceiveQty = BigDecimal.valueOf(0);
    @ApiModelProperty("已料废调换数量")
    private BigDecimal exchangedQty = BigDecimal.valueOf(0);
    @ApiModelProperty("过账料废调换数量")
    private BigDecimal stockInExchangedQty = BigDecimal.valueOf(0);

    @ApiModelProperty("单据状态Meaning")
    private String instructionDocStatusMeaning;
    @ApiModelProperty("单据行状态Meaning")
    private String instructionStatusMeaning;

    @ApiModelProperty("料废调换库位ID")
    private String exchangeLocatorId;
    @ApiModelProperty("料废调换库位code")
    private String exchangeLocatorCode;
    @ApiModelProperty("目标仓库")
    private String transferWarehouseId;
    @ApiModelProperty("销售订单号")
    private String soNum;
    @ApiModelProperty("销售订单行号")
    private String soLineNum;

    @ApiModelProperty(value = "实际存储货位")
    private String actualLocatorName;

    @ApiModelProperty(value = "实际存储货位编码")
    private String actualLocatorCode;
    
    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;
}
