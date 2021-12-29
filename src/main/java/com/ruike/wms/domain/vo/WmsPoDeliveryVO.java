package com.ruike.wms.domain.vo;

import com.ruike.wms.domain.entity.WmsPoDeliveryRel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 采购订单查询返回的VO
 * @author: han.zhang
 * @create: 2020/03/19 11:06
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsPoDeliveryVO extends WmsPoDeliveryRel {

    private static final long serialVersionUID = 8480309681834641699L;

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
    @ApiModelProperty(value = "送货单状态")
    private String instructionDocStatus;
    @ApiModelProperty(value = "采购订单号")
    private String poNum;
    @ApiModelProperty(value = "到货时间从")
    private String demandTimeFrom;
    @ApiModelProperty(value = "到货时间至")
    private String demandTimeTo;
    @ApiModelProperty(value = "采购订单号")
    private String instructionLineNum;
    @ApiModelProperty(value = "采购订单类型")
    private List<String> typeList;
}