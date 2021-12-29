package com.ruike.wms.domain.vo;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * WmsObjectTransactionVO
 *
 * @author liyuan.lv@hand-china.com 2020/04/14 18:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WmsObjectTransactionVO implements Serializable {
    private static final long serialVersionUID = -9095786875101070414L;
    @ApiModelProperty("事件ID")
    private String eventId;
    @ApiModelProperty("事务类型")
    private String transactionTypeCode;
    @ApiModelProperty("事务原因")
    private String transactionReasonCode;
    @ApiModelProperty(value = "事务时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private Date transactionTime;
    @ApiModelProperty("事务数量")
    private BigDecimal transactionQty;
    @ApiModelProperty("目标库位Id")
    private String transferLocatorId;
    @ApiModelProperty("目标仓库Id")
    private String transferWarehouseId;
    @ApiModelProperty("目标库位编码")
    private String transferLocatorCode;
    @ApiModelProperty("来源单据类型")
    private String sourceDocType;
    @ApiModelProperty("来源单据Id")
    private String sourceDocId;
    @ApiModelProperty("来源单据行Id")
    private String sourceDocLineId;
    @ApiModelProperty("来源单据号")
    private String sourceDocNum;
    @ApiModelProperty("来源单据行号")
    private String sourceDocLineNum;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("成本中心编码")
    private String costCenterCode;
    @ApiModelProperty("内部订单编码")
    private String internalOrderCode;
    @ApiModelProperty("目标批次")
    private String transferLot;
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    @ApiModelProperty("货位ID")
    private String locatorId;
    @ApiModelProperty("移动类型")
    private String moveType;
    @ApiModelProperty("移动原因")
    private String moveReason;
    @ApiModelProperty("预留/需求的编号")
    private String bomReserveNum;
    @ApiModelProperty("预留/需求的项目编号")
    private String bomReserveLineNum;
    @ApiModelProperty("销售订单号")
    private String soNum;
    @ApiModelProperty("销售订单行号")
    private String soLineNum;
    @ApiModelProperty("目标销售订单号")
    private String transferSoNum;
    @ApiModelProperty("目标销售订单行号")
    private String transferSoLineNum;
    @ApiModelProperty("采购订单号")
    private String poNum;
    @ApiModelProperty("采购订单行号")
    private String poLineNum;
    @ApiModelProperty("采购订单号")
    private String poId;
    @ApiModelProperty("采购订单行号")
    private String poLineId;
    @ApiModelProperty("容器id")
    private String containerId;
    @ApiModelProperty(value = "销售订单id")
    private String saleDocId;
    @ApiModelProperty(value = "销售订单行id")
    private String saleDocLineId;
    @ApiModelProperty(value = "批次号")
    private String lotCode;

}
