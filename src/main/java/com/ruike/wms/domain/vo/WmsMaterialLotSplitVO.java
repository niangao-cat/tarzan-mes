package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * WmsMaterialLotSplitVO
 *
 * @author: chaonan.hu@hand-china.com 2020-09-07 15:36:14
 **/
@Data
public class WmsMaterialLotSplitVO implements Serializable {
    private static final long serialVersionUID = -3045039453323640469L;

    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;

    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "主单位ID")
    private String primaryUomId;

    @ApiModelProperty(value = "主单位编码")
    private String primaryUomCode;

    @ApiModelProperty(value = "主单位数量")
    private BigDecimal primaryUomQty;

    @ApiModelProperty(value = "质量状态")
    @LovValue(value = "WMS.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;

    @ApiModelProperty(value = "质量状态含义")
    private String qualityStatusMeaning;

    @ApiModelProperty(value = "批次")
    private String lot;

    @ApiModelProperty(value = "供应商ID")
    private String supplierId;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;

}