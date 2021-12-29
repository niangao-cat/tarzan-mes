package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;
import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-04-15 17:19
 */
@Data
public class WmsMaterialOnShelfBarCodeDTO {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "物料批")
    private String materialLotCode;
    @ApiModelProperty(value = "工厂ID")
    private String siteId;
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
    @ApiModelProperty(value = "当前容器")
    private String currentContainerId;
    @ApiModelProperty(value = "顶层容器")
    private String topContainerId;
    @ApiModelProperty(value = "条码状态")
    @LovValue(lovCode = "MT.MTLOT.STATUS", meaningField = "materialLotStatusMeaning", defaultMeaning = "无")
    private String materialLotStatus;
    @ApiModelProperty(value = "条码状态含义")
    private String materialLotStatusMeaning;
    @ApiModelProperty(value = "批")
    private String lot;
    @ApiModelProperty(value = "缓存标记")
    private String cacheFlag;
    @ApiModelProperty(value = "物料批当前所在货位标识ID")
    private String locatorId;
    @ApiModelProperty(value = "货位编码")
    private String locatorCode;
    @ApiModelProperty(value = "货位名称")
    private String locatorName;
    @ApiModelProperty(value = "实绩货位")
    private String actualLocator;
    @ApiModelProperty(value = "物料批冻结标识")
    private String freezeFlag;
    @ApiModelProperty(value = "盘点停用标识")
    private String stocktakeFlag;
    @ApiModelProperty(value = "在制品标识")
    private String mfFlag;
    @ApiModelProperty("单据行")
    WmsMaterialOnShelfDocLineDTO orderLineDto;
}
