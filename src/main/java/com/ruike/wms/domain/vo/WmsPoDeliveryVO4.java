package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;

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
public class WmsPoDeliveryVO4 {

    private static final long serialVersionUID = 8480309681834641699L;

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