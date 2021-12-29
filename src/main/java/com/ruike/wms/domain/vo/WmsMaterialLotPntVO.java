package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Classname MaterialLotPntVO
 * @Description 业务类型
 * @Date 2019/12/6 15:37
 * @Author by {weihua.liao}
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WmsMaterialLotPntVO {

    @ApiModelProperty(value = "条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "条码号")
    private String materialLotCode;

    @ApiModelProperty(value = "关联物料")
    private String materialId;

    @ApiModelProperty(value = "物料号")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "批次号")
    private String lot;

    @ApiModelProperty(value = "数量")
    private BigDecimal primaryUomQty;

    @ApiModelProperty(value = "单位")
    private String primaryUomCode;

    @ApiModelProperty(value = "供应商号")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    private String barcodeImg;

    @ApiModelProperty(value = "色温")
    private String colorBin;

    @ApiModelProperty(value = "亮度")
    private String lightBin;

    @ApiModelProperty(value = "电压")
    private String voltageBin;

    @ApiModelProperty(value = "膨胀系数")
    private String expansionCoefficients;

}
