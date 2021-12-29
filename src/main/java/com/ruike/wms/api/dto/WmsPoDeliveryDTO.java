package com.ruike.wms.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 创建送货单
 * @author: han.zhang
 * @create: 2020/03/27 19:25
 */
@Getter
@Setter
@ToString
public class WmsPoDeliveryDTO implements Serializable {

    private static final long serialVersionUID = -2610929043691895048L;

    @ApiModelProperty(value = "送货单号")
    private String instructionDocNum;
    @ApiModelProperty(value = "单据类型")
    private String instructionDocType;
    @ApiModelProperty(value = "工厂")
    private String siteId;
    @ApiModelProperty(value = "供应商")
    private String supplierId;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商")
    private String supplierSiteId;
    @ApiModelProperty(value = "客户")
    private String customerId;
    @ApiModelProperty(value = "送货地址")
    private String customerSiteId;
    @ApiModelProperty(value = "单据状态")
    private String instructionDocStatus;
    @ApiModelProperty(value = "到货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date demandTime;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "标记")
    private String mark;

    @ApiModelProperty(value = "送货单行数据")
    private List<DeliveryOrderLineDTO> lineDTOList;

    @Getter
    @Setter
    @ToString
    public static class DeliveryOrderLineDTO{
        @ApiModelProperty(value = "物料")
        private String materialId;
        @ApiModelProperty(value = "物料版本")
        private String materialVersion;
        @ApiModelProperty(value = "制单数量")
        private BigDecimal quantity;
        @ApiModelProperty(value = "单位")
        private String uomId;
        @ApiModelProperty(value = "送货单行状态")
        private String instructionStatus;
        @ApiModelProperty(value = "料废调换数量")
        private BigDecimal exchangeQty;
        @ApiModelProperty(value = "料废调换标识")
        private String exchangeFlag;
        @ApiModelProperty(value = "特采标识")
        private String uaiFlag;
        @ApiModelProperty(value = "目标库位id")
        private String toLocatorId;
        @ApiModelProperty(value = "送货单行对应的采购订单头id和行Id")
        private List<OrderIdDTO> orderIdDTOS;
        @ApiModelProperty(value = "销售订单")
        private String soNum;
        @ApiModelProperty(value = "销售订单行")
        private String soLineNum;

        @Getter
        @Setter
        @ToString
        public static class OrderIdDTO{
            @ApiModelProperty(value = "采购订单头id")
            private String poId;
            @ApiModelProperty(value = "采购订单行id")
            private String poLineId;
            @ApiModelProperty(value = "每条采购订单行的制单数量")
            private BigDecimal quantity;
        }
    }
}