package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;

/**
 * 物料批及拓展属性
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 17:03
 */
@Data
public class WmsMaterialLotAttrVO {
    @ApiModelProperty("唯一标识")
    private String materialLotId;
    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;
    @ApiModelProperty(value = "有效状态")
    private String enableFlag;
    @ApiModelProperty(value = "冻结标识")
    private String freezeFlag;
    @ApiModelProperty(value = "盘点停用标识")
    private String stocktakeFlag;
    @ApiModelProperty(value = "在制品标志")
    private String mfFlag;
    @ApiModelProperty(value = "质量状态")
    @LovValue(lovCode = "WMS.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;
    @ApiModelProperty(value = "质量状态含义")
    private String qualityStatusMeaning;
    @ApiModelProperty(value = "物料标识ID")
    private String materialId;
    @ApiModelProperty(value = "物料标识ID")
    private String materialCode;
    @ApiModelProperty(value = "物料标识ID")
    private String materialName;
    @ApiModelProperty(value = "主计量单位Id")
    private String primaryUomId;
    @ApiModelProperty(value = "批次")
    private String lot;
    @ApiModelProperty(value = "主计量单位")
    private String primaryUomCode;
    @ApiModelProperty(value = "主计量单位数量")
    private BigDecimal primaryUomQty;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "条码状态")
    @LovValue(lovCode = "WMS.MTLOT.STATUS", meaningField = "statusMeaning")
    private String status;
    @ApiModelProperty(value = "条码状态含义")
    private String statusMeaning;
    @ApiModelProperty(value = "销售订单")
    private String soNum;
    @ApiModelProperty(value = "销售订单行")
    private String soLineNum;
    @ApiModelProperty(value = "工厂ID")
    private String plantId;
    @ApiModelProperty(value = "工厂编码")
    private String plantCode;
    @ApiModelProperty(value = "货位ID")
    private String locatorId;
    @ApiModelProperty(value = "货位编码")
    private String locatorCode;
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    @ApiModelProperty(value = "虚拟标志")
    private String virtualFlag;
    @ApiModelProperty(value = "EO标识")
    private String eoId;
    @ApiModelProperty(value = "当前容器标识")
    private String currentContainerId;
}
