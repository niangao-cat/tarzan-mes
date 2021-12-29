package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-04-15 17:19
 */
@Data
public class WmsCostCtrMaterialDTO3 {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;
    @ApiModelProperty(value = "工厂ID")
    private String siteId;
    @ApiModelProperty(value = "工厂")
    private String siteCode;
    @ApiModelProperty(value = "有效性标识")
    private String enableFlag;
    @ApiModelProperty(value = "质量状态")
    @LovValue(lovCode = "MT.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning", defaultMeaning = "无")
    private String qualityStatus;
    @ApiModelProperty(value = "质量状态含义")
    private String qualityStatusMeaning;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "物料单位ID")
    private String primaryUomId;
    @ApiModelProperty(value = "物料单位编码")
    private String primaryUomCode;
    @ApiModelProperty(value = "物料单位名称")
    private String primaryUomName;
    @ApiModelProperty(value = "主单位数量")
    private BigDecimal primaryUomQty;
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    @ApiModelProperty(value = "仓库CODE")
    private String warehouseCode;
    @ApiModelProperty(value = "货位ID")
    private String locatorId;
    @ApiModelProperty(value = "货位CODE")
    private String locatorCode;
    @ApiModelProperty(value = "容器ID")
    private String containerId;
    @ApiModelProperty(value = "容器编码")
    private String containerCode;
    @ApiModelProperty(value = "批")
    private String lot;
    @ApiModelProperty(value = "物料批冻结标识")
    private String freezeFlag;
    @ApiModelProperty(value = "盘点停用标识")
    private String stocktakeFlag;
    @ApiModelProperty(value = "缓存标记")
    private String cacheFlag;
    @ApiModelProperty(value = "扫描货位ID")
    private String cacheLocatorId;
    @ApiModelProperty(value = "扫描货位CODE")
    private String cacheLocatorCode;
    @ApiModelProperty(value = "扫描货位名称")
    private String cacheLocatorName;
    @ApiModelProperty(value = "当前条码下包含此物料对应条码个数")
    private int addCodeQty;
    @ApiModelProperty(value = "当前条码下包含此物料总数")
    private BigDecimal addQuantity;
    @ApiModelProperty(value = "是否为容器条码")
    private String isContainerCode;
    @ApiModelProperty(value = "在制品标识")
    private String mfFlag;
}
