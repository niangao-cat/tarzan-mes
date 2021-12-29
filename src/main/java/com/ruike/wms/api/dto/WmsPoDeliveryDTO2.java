package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 创建送货单
 * @author: han.zhang
 * @create: 2020/03/27 19:25
 */
@Getter
@Setter
@ToString
public class WmsPoDeliveryDTO2 implements Serializable {

    private static final long serialVersionUID = 7223660658247283795L;
    @ApiModelProperty(value = "送货单号")
    private String instructionDocNum;
    @ApiModelProperty(value = "供应商")
    private String supplierId;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "物料")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "单据状态")
    private String instructionDocStatus;
    @ApiModelProperty(value = "采购订单号")
    private String instructionNum;
    @ApiModelProperty(value = "到货时间从")
    private String demandTimeFrom;
    @ApiModelProperty(value = "到货时间至")
    private String demandTimeTo;

}