package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @program: tarzan-mes
 * @description: 采购订单查询返回的VO
 * @author: han.zhang
 * @create: 2020/03/19 11:06
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsPoDeliveryVO2 {

    private static final long serialVersionUID = 8480309681834641699L;
    @ApiModelProperty(value = "送货单ID")
    private String instructionDocId;
    @ApiModelProperty(value = "送货单号")
    private String instructionDocNum;
    @ApiModelProperty(value = "送货单状态")
    private String instructionDocStatus;
    @ApiModelProperty(value = "供应商")
    private String supplierId;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "预计送达日期")
    private LocalDate demandTime;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "物料")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "制单数量")
    private BigDecimal quantity;
    @ApiModelProperty(value = "单位")
    private String uomCode;
    @ApiModelProperty(value = "行状态")
    private String instructionStatus;
    @ApiModelProperty(value = "料废调换数量")
    private BigDecimal exchangeQty;
    @ApiModelProperty(value = "采购订单行号")
    private String poLineNum;
    @ApiModelProperty(value = "特采标识")
    private String uaiFlag;
    @ApiModelProperty(value = "已接收数量")
    private BigDecimal receivedQty;
    @ApiModelProperty(value = "已料废调换数量")
    private BigDecimal exchangedQty;
}