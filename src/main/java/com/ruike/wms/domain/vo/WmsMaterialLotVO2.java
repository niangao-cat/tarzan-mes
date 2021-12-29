package com.ruike.wms.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import org.hzero.boot.platform.lov.annotation.LovValue;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * WmsMaterialLotVO
 *
 * @author liyuan.lv@hand-china.com 2020/04/28 15:09
 */
@Data
public class WmsMaterialLotVO2 implements Serializable {

    private static final long serialVersionUID = -2806019374166326635L;
    @ApiModelProperty("工厂Id")
    private String siteId;
    @ApiModelProperty("工厂编码")
    private String siteCode;
    @ApiModelProperty("物料批Id")
    private String materialLotId;
    @ApiModelProperty("物料批编码")
    private String materialLotCode;
    @ApiModelProperty("是否有效")
    private String enableFlag;
    @ApiModelProperty("质量状态")
    @LovValue(value = "WMS.MTLOT.QUALITY_STATUS",meaningField ="qualityStatusMeaning" )
    private String qualityStatus;
    @ApiModelProperty("物料Id")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("主单位Id")
    private String primaryUomId;
    @ApiModelProperty("主单位编码")
    private String primaryUomCode;
    @ApiModelProperty("主单位名称")
    private String primaryUomName;
    @ApiModelProperty("主数量")
    private BigDecimal primaryUomQty;
    @ApiModelProperty("货位Id")
    private String locatorId;
    @ApiModelProperty("库位编码")
    private String locatorCode;
    @ApiModelProperty("仓库Id")
    private String warehouseId;
    @ApiModelProperty("仓库编码")
    private String warehouseCode;
    @ApiModelProperty("批次")
    private String lot;
    @ApiModelProperty("供应商Id")
    private String supplierId;
    @ApiModelProperty("供应商编码")
    private String supplierCode;
    @ApiModelProperty("供应商名称")
    private String supplierName;
    @ApiModelProperty("供应商地点Id")
    private String supplierSiteId;
    @ApiModelProperty("物料批冻结标识")
    private String freezeFlag;
    @ApiModelProperty("条码状态")
    @LovValue(value = "WMS.MTLOT.STATUS",meaningField ="materialLotStatusMeaning" )
    private String materialLotStatus;
    @ApiModelProperty("质量状态Meaning")
    private String qualityStatusMeaning;
    @ApiModelProperty("条码状态Meaning")
    private String materialLotStatusMeaning;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("冻结日期")
    private String freezeDate;
    @ApiModelProperty("供应商批次")
    private String supplierLot;
    @ApiModelProperty("销售订单")
    private String soNum;

}
