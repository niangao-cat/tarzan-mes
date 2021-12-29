package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 物料行信息
 * @Author tong.li
 * @Date 2020/5/8 11:42
 * @Version 1.0
 */
@Data
public class WmsMaterialWasteExchangeDTO3 implements Serializable {
    private static final long serialVersionUID = -1167422628555312779L;


    @ApiModelProperty(value = "实物条码")
    private String barCode;
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
    @ApiModelProperty(value = "仓库ID")
    private String parentLocatorId;
    @ApiModelProperty(value = "仓库编码")
    private String parentLocatorCode;
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
    @ApiModelProperty(value = "工厂ID")
    private String siteId;

    @ApiModelProperty(value = "供应商地点编码")
    private String supplierSiteCode;
    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "批")
    private String lot;

}
