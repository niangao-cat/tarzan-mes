package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeTimeMaterialSplitVO
 *
 * @author: chaonan.hu@hand-china.com 2020/9/12 11:34:23
 **/
@Data
public class HmeTimeMaterialSplitVO implements Serializable {
    private static final long serialVersionUID = 3870487611136889166L;

    @ApiModelProperty(value = "物料批Id")
    private String materialLotId;

    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "数量")
    private BigDecimal primaryUomQty;

    @ApiModelProperty(value = "主单位Id")
    private String primaryUomId;

    @ApiModelProperty(value = "主单位编码")
    private String primaryUomCode;

    @ApiModelProperty(value = "批次")
    private String lot;

    @ApiModelProperty(value = "供应商Id")
    private String supplierId;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "有效期起")
    private String dateTimeFrom;

    @ApiModelProperty(value = "有效期至")
    private String dateTimeTo;

    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;

    @ApiModelProperty(value = "开封有效时间")
    private String openEffectiveTime;

    @ApiModelProperty(value = "开封有效单位")
    private String openEffectiveUom;
}
