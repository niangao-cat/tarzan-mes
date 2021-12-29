package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ItfSrmMaterialWasteIfaceSyncDTO {

    @ApiModelProperty(value = "供应商")
    private String vendorCode;

    @ApiModelProperty(value = "物料编码")
    private String itemCode;

    @ApiModelProperty(value = "未发出数量")
    private String primayChangeQty;

    @ApiModelProperty(value = "发出数量")
    private String primaryUomQty;

    @ApiModelProperty(value = "发出单位")
    private String primaryUom;

    @ApiModelProperty(value = "工厂")
    private String shipToOrganization;

    @ApiModelProperty(value = "是否发送成功")
    private String status;

    @ApiModelProperty(value = "错误信息")
    private String message;
}
