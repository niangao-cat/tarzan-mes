package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2020/11/11 13:56
 */
@Data
public class WmsPurchaseCodeDetailsVO implements Serializable {

    private static final long serialVersionUID = -8049292462789262847L;

    @ApiModelProperty(value = "单据行id")
    private String instructionId;

    @ApiModelProperty(value = "执行数量")
    private BigDecimal actualQty;

    @ApiModelProperty(value = "条码id")
    private String materialLotId;

    @ApiModelProperty(value = "物料id")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "条码")
    private String materialLotCode;

    @ApiModelProperty(value = "数量")
    private BigDecimal primaryUomQty;

    @ApiModelProperty(value = "单位编码")
    private String uomCode;

    @ApiModelProperty(value = "单位名称")
    private String uomName;

    @ApiModelProperty(value = "质量状态")
    @LovValue(value = "MT.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;

    @ApiModelProperty(value = "质量状态含义")
    private String qualityStatusMeaning;

    @ApiModelProperty(value = "批次")
    private String lot;

    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;

    @ApiModelProperty(value = "条码状态")
    private String codeStatus;

    @ApiModelProperty(value = "站点")
    private String siteId;

    @ApiModelProperty(value = "货位")
    private String locatorId;

    @ApiModelProperty(value = "容器id")
    private String containerId;
}
