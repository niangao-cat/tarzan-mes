package com.ruike.wms.domain.vo;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * WmsObjectTransactionRequestVO
 *
 * @author liyuan.lv@hand-china.com 2020/04/09 14:47
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsObjectTransactionRequestVO {
    @ApiModelProperty(value = "事务ID")
    private String transactionId;
    @NotNull
    @ApiModelProperty(value = "事务类型编码", required = true)
    private String transactionTypeCode;
    @NotNull
    @ApiModelProperty(value = "事件ID", required = true)
    private String eventId;
    @ApiModelProperty(value = "事务条码")
    private String barcode;
    @ApiModelProperty(value = "工厂编码", required = true)
    @NotNull
    private String plantCode;
    @ApiModelProperty(value = "物料编码", required = true)
    @NotNull
    private String materialCode;
    @ApiModelProperty(value = "事务数量")
    @NotNull
    private BigDecimal transactionQty;
    @ApiModelProperty(value = "批次号")
    private String lotNumber;
    @ApiModelProperty(value = "事务单位")
    private String transactionUom;
    @ApiModelProperty(value = "事务时间", required = true)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @NotNull
    private Date transactionTime;
    @ApiModelProperty(value = "事务原因")
    private String transactionReasonCode;
    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;
    @ApiModelProperty(value = "货位编码")
    private String locatorCode;
    @ApiModelProperty(value = "目标工厂编码")
    private String transferPlantCode;
    @ApiModelProperty(value = "目标仓库编码")
    private String transferWarehouseCode;
    @ApiModelProperty(value = "目标货位编码")
    private String transferLocatorCode;
    @ApiModelProperty(value = "成本中心编码")
    private String costCenterCode;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商地点编码")
    private String supplierSiteCode;
    @ApiModelProperty(value = "客户编码")
    private String customerCode;
    @ApiModelProperty(value = "客户地点编码")
    private String customerSiteCode;
    @ApiModelProperty(value = "来源单据类型")
    private String sourceDocType;
    @ApiModelProperty(value = "来源单据号")
    private String sourceDocNum;
    @ApiModelProperty(value = "来源单据行号")
    private String sourceDocLineNum;
    @ApiModelProperty(value = "工单号")
    private String workOrderNum;
    @ApiModelProperty(value = "工序号")
    private String operationSequence;
    @ApiModelProperty(value = "生产线编码")
    private String prodLineCode;

    @ApiModelProperty(value = "来源单据Id")
    private String sourceDocId;
    @ApiModelProperty(value = "来源单据行Id")
    private String sourceDocLineId;
    @ApiModelProperty(value = "条码Id")
    private String materialLotId;
    @ApiModelProperty(value = "物料Id")
    private String materialId;
    @ApiModelProperty(value = "来源货位Id")
    private String locatorId;
    @ApiModelProperty(value = "目标货位Id")
    private String transferLocatorId;
    @ApiModelProperty(value = "工厂Id")
    private String plantId;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "租户Id")
    private Long tenantId;
    @ApiModelProperty(value = "仓库id")
    private String warehouseId;
    @ApiModelProperty(value = "目标仓库id")
    private String transferWarehouseId;
    @ApiModelProperty(value = "目标工厂id")
    private String transferPlantId;

    @ApiModelProperty(value = "接收批")
    private String deliveryBatch;
    @ApiModelProperty(value = "销售订单id")
    private String saleDocId;
    @ApiModelProperty(value = "销售订单行id")
    private String saleDocLineId;
    @ApiModelProperty(value = "目标销售订单id")
    private String transferSaleDocId;
    @ApiModelProperty(value = "目标销售订单行id")
    private String transferSaleDocLineId;
    @ApiModelProperty(value = "制造订单号")
    private String makeOrderNum;
    @ApiModelProperty(value = "所有者类型")
    private String ownerType;
    @ApiModelProperty(value = "是否合并", required = true)
    @NotNull
    private String mergeFlag;

    @ApiModelProperty(value = "内部订单号")
    private String insideOrder;
    @ApiModelProperty(value = "移动类型")
    private String moveType;
    @ApiModelProperty(value = "移动原因")
    private String moveReason;
    @ApiModelProperty(value = "预留/需求的编号")
    private String bomReserveNum;
    @ApiModelProperty(value = "预留/需求的项目编号")
    private String bomReserveLineNum;
    @ApiModelProperty(value = "目标销售订单号")
    private String transferSoNum;
    @ApiModelProperty(value = "目标销售订单行号")
    private String transferSoLineNum;
    @ApiModelProperty(value = "采购订单号")
    private String poNum;
    @ApiModelProperty(value = "采购订单行号")
    private String poLineNum;
    @ApiModelProperty(value = "采购订单号ID")
    private String poId;
    @ApiModelProperty(value = "采购订单行号ID")
    private String poLineId;

    @ApiModelProperty(value = "录产生事务物料对应的目标批次")
    private String transferLotNumber;
    @ApiModelProperty(value = "记录产生事务的容器id")
    private String containerId;
    @ApiModelProperty(value = "容器编码")
    private String containerCode;
    @ApiModelProperty(value = "记录产生事务是否为特殊库存标识")
    private String specStockFlag;
    @ApiModelProperty(value = "SAP移动事物分配代码")
    private String gmcode;
    @ApiModelProperty(value = "记录产生事务的销售订单号")
    private String soNum;
    @ApiModelProperty(value = "记录产生事务的销售订单行号")
    private String soLineNum;
    @ApiModelProperty(value = "记录产生事务的SN号字段")
    private String snNum;


    @ApiModelProperty(value = "扩展字段1")
    private String attribute1;
    @ApiModelProperty(value = "扩展字段2")
    private String attribute2;
    @ApiModelProperty(value = "扩展字段3")
    private String attribute3;
    @ApiModelProperty(value = "扩展字段4")
    private String attribute4;
    @ApiModelProperty(value = "扩展字段5")
    private String attribute5;
    @ApiModelProperty(value = "扩展字段6")
    private String attribute6;
    @ApiModelProperty(value = "扩展字段7")
    private String attribute7;
    @ApiModelProperty(value = "扩展字段8")
    private String attribute8;
    @ApiModelProperty(value = "扩展字段9")
    private String attribute9;
    @ApiModelProperty(value = "扩展字段10")
    private String attribute10;
    @ApiModelProperty(value = "扩展字段11")
    private String attribute11;
    @ApiModelProperty(value = "扩展字段12")
    private String attribute12;
    @ApiModelProperty(value = "扩展字段13")
    private String attribute13;
    @ApiModelProperty(value = "扩展字段14")
    private String attribute14;
    @ApiModelProperty(value = "扩展字段15")
    private String attribute15;
    @ApiModelProperty(value = "扩展字段16")
    private String attribute16;
    @ApiModelProperty(value = "扩展字段17")
    private String attribute17;
    @ApiModelProperty(value = "扩展字段18")
    private String attribute18;
    @ApiModelProperty(value = "扩展字段19")
    private String attribute19;
    @ApiModelProperty(value = "扩展字段20")
    private String attribute20;
    @ApiModelProperty(value = "扩展字段21")
    private String attribute21;
    @ApiModelProperty(value = "扩展字段22")
    private String attribute22;
    @ApiModelProperty(value = "扩展字段23")
    private String attribute23;
    @ApiModelProperty(value = "扩展字段24")
    private String attribute24;
    @ApiModelProperty(value = "扩展字段25")
    private String attribute25;
    @ApiModelProperty(value = "扩展字段26")
    private String attribute26;
    @ApiModelProperty(value = "扩展字段27")
    private String attribute27;
    @ApiModelProperty(value = "扩展字段28")
    private String attribute28;
    @ApiModelProperty(value = "扩展字段29")
    private String attribute29;
    @ApiModelProperty(value = "扩展字段30")
    private String attribute30;
    @ApiModelProperty(value = "扩展字段31")
    private String attribute31;
    @ApiModelProperty(value = "扩展字段32")
    private String attribute32;
    @ApiModelProperty(value = "扩展字段33")
    private String attribute33;
    @ApiModelProperty(value = "扩展字段34")
    private String attribute34;
    @ApiModelProperty(value = "扩展字段35")
    private String attribute35;
    @ApiModelProperty(value = "扩展字段36")
    private String attribute36;
    @ApiModelProperty(value = "扩展字段37")
    private String attribute37;
    @ApiModelProperty(value = "扩展字段38")
    private String attribute38;
    @ApiModelProperty(value = "扩展字段39")
    private String attribute39;
    @ApiModelProperty(value = "扩展字段40")
    private String attribute40;
}
