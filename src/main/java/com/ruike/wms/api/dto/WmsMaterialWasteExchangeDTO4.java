package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 明细按钮  返回数据头
 * @Author tong.li
 * @Date 2020/5/8 18:46
 * @Version 1.0
 */
@Data
public class WmsMaterialWasteExchangeDTO4 implements Serializable {
    private static final long serialVersionUID = -6144577591741548952L;

    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
    @ApiModelProperty(value = "仓库ID")
    private String locatorId;
    @ApiModelProperty(value = "仓库CODE")
    private String locatorCode;
    @ApiModelProperty(value = "累计数量")
    private BigDecimal cumulativeQty;
    @ApiModelProperty(value = "条码个数")
    private int codeQty;
    @ApiModelProperty(value = "物料单位ID")
    private String primaryUomId;
    @ApiModelProperty(value = "物料单位编码")
    private String primaryUomCode;
    @ApiModelProperty(value = "物料单位名称")
    private String primaryUomName;

    @ApiModelProperty(value = "明细行")
    private List<WmsMaterialWasteExchangeDTO5> detailLine;
}
