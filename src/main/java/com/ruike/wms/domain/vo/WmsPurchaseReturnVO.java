package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/11/9 14:28
 */
@Data
public class WmsPurchaseReturnVO implements Serializable {

    private static final long serialVersionUID = -5250015869706604634L;

    @ApiModelProperty(value = "退货采购订单")
    private String instructionDocNum;

    @ApiModelProperty(value = "工厂ID")
    private String siteId;

    @ApiModelProperty(value = "单据状态")
    private String instructionDocStatus;

    @ApiModelProperty(value = "供应商ID")
    private String supplierId;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料id")
    private String materialId;

    @ApiModelProperty(value = "退货时间从")
    private String demandTimeFrom;

    @ApiModelProperty(value = "退货时间至")
    private String demandTimeTo;

    @ApiModelProperty(value = "单据类型列表")
    private List<String> docTypeList;
}
