package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description 明细行
 * @Author tong.li
 * @Date 2020/5/8 18:47
 * @Version 1.0
 */
@Data
public class WmsMaterialWasteExchangeDTO5 implements Serializable {
    private static final long serialVersionUID = 3016412611448409158L;

    @ApiModelProperty(value = "实物条码")
    private String barCode;
    @ApiModelProperty(value = "累计数量")
    private BigDecimal cumulativeQty;
    @ApiModelProperty(value = "物料单位ID")
    private String primaryUomId;
    @ApiModelProperty(value = "物料单位编码")
    private String primaryUomCode;
    @ApiModelProperty(value = "物料单位名称")
    private String primaryUomName;
    @ApiModelProperty(value = "仓库ID")
    private String locatorId;
    @ApiModelProperty(value = "仓库CODE")
    private String locatorCode;
    @ApiModelProperty(value = "批")
    private String lot;
    @ApiModelProperty(value = "质量状态")
    @LovValue(lovCode = "MT.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning", defaultMeaning = "无")
    private String qualityStatus;
    @ApiModelProperty(value = "质量状态含义")
    private String qualityStatusMeaning;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
}
