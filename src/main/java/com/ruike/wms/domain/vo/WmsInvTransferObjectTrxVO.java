package com.ruike.wms.domain.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;

/**
 * WmsObjectTransactionVO
 *
 * @author jiangling.zheng@hand-china.com 2020-04-27 09:50:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WmsInvTransferObjectTrxVO implements Serializable {
    private static final long serialVersionUID = -9095786875101070414L;
    @ApiModelProperty("事件ID")
    private String eventId;
    @ApiModelProperty("事务类型")
    private String transactionTypeCode;
    @ApiModelProperty(value = "事务时间", required = true)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @NotNull
    private Date transactionTime;
    @ApiModelProperty("事务数量")
    private BigDecimal transactionQty;
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
    @ApiModelProperty(value = "目标工厂id")
    private String transferPlantId;
    @ApiModelProperty(value = "目标工厂编码")
    private String transferPlantCode;
    @ApiModelProperty(value = "目标仓库id")
    private String transferWarehouseId;
    @ApiModelProperty(value = "目标仓库编码")
    private String transferWarehouseCode;
    @ApiModelProperty(value = "目标货位Id")
    private String transferLocatorId;
    @ApiModelProperty(value = "目标货位编码")
    private String transferLocatorCode;
    @ApiModelProperty(value = "单位")
    private String transactionUom;
    @ApiModelProperty("来源仓库ID")
    private String warehouseId;
    @ApiModelProperty(value = "移动类型")
    private String moveType;
    @ApiModelProperty(value = "容器ID")
    private String containerId;
    @ApiModelProperty(value = "容器编码")
    private String containerCode;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("批次")
    private String lotNumber;
    @ApiModelProperty("转移批次")
    private String transferLotNumber;
    @ApiModelProperty(value = "事务原因")
    private String transactionReasonCode;
    @ApiModelProperty(value = "销售订单")
    private String soNum;
    @ApiModelProperty(value = "销售订单行")
    private String soLineNum;
}
