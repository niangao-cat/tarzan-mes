package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WmsStandingWarehouseEntryReviewDTO2 implements Serializable {
    private static final long serialVersionUID = 4616701550760835262L;
    @ApiModelProperty(value = "tenantId")
    private Long tenantId;
    @ApiModelProperty(value = "物料批Id")
    private String  materialLotId;
    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;
    @ApiModelProperty(value = "容器")
    private String materialContainerCode;
    @ApiModelProperty(value = "物料Id")
    private Long materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "数量")
    private BigDecimal materialQty;
    @ApiModelProperty(value = "复核标识")
    private String checkConfirmFlag;
    @ApiModelProperty(value = "复核状态标识")
    private String checkConfirm;
    @ApiModelProperty(value = "质量状态")
    private String qualityStatus;
    @ApiModelProperty(value = "freezeFlag")
    private String freezeFlag;
    @ApiModelProperty(value = "stocktakeFlag")
    private String stocktakeFlag;
    @ApiModelProperty(value = "货位Id")
    private Long locatorId;
    @ApiModelProperty(value = "货位编码")
    private String locatorCode;
    @ApiModelProperty(value = "货位名称")
    private String locatorName;

}
