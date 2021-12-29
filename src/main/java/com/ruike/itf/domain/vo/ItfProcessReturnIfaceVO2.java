package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/11/1 16:20
 */
@Data
public class ItfProcessReturnIfaceVO2 implements Serializable {

    private static final long serialVersionUID = 7958907908554798414L;

    @ApiModelProperty("工单Id")
    private String workOrderId;

    @ApiModelProperty("工单类型")
    private String workOrderType;

    @ApiModelProperty("eo状态")
    private String eoStatus;

    @ApiModelProperty("工单类型含义")
    private String workOrderTypeMeaning;

    @ApiModelProperty("eo状态含义")
    private String eoStatusMeaning;

    @ApiModelProperty("条码")
    private String materialLotCode;

    @ApiModelProperty("工单数量")
    private BigDecimal woQuantity;

    @ApiModelProperty("SAP料号")
    private String sapMaterialName;

    @ApiModelProperty("SAP料号编码")
    private String sapMaterialCode;

    @ApiModelProperty(value = "产品类型")
    private String snMaterialCode;

    @ApiModelProperty(value = "物料描述")
    private String snMaterialName;
}
