package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-04-27 09:50
 */
@Data
public class WmsInvTransferDTO2 {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "单据行ID")
    private String instructionId;
    @ApiModelProperty(value = "单据行编码")
    private String instructionNum;
    @ApiModelProperty(value = "单据行状态")
    @LovValue(lovCode = "WMS.STOCK_ALLOCATION_DOC_LINE.STATUS",meaningField="lineStatusMeaning",defaultMeaning = "无")
    private String instructionStatus;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "单据行状态说明")
    private String lineStatusMeaning;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "制单数量")
    private BigDecimal quantity;
    @ApiModelProperty(value = "实绩数量")
    private BigDecimal actualQty;
    @ApiModelProperty(value = "已扫描条码总数量")
    private BigDecimal cumExecuteQty;
    @ApiModelProperty(value = "来源工厂ID")
    private String fromSiteId;
    @ApiModelProperty(value = "目标工厂ID")
    private String toSiteId;
    @ApiModelProperty(value = "目标工厂编码")
    private String toSiteCode;
    @ApiModelProperty(value = "目标工厂名称")
    private String toSiteName;
    @ApiModelProperty(value = "发出仓库ID")
    private String fromWarehouseId;
    @ApiModelProperty(value = "发出仓库编码")
    private String fromWarehouseCode;
    @ApiModelProperty(value = "发出仓库名称")
    private String fromWarehouseName;
    @ApiModelProperty(value = "目标仓库ID")
    private String toWarehouseId;
    @ApiModelProperty(value = "目标仓库编码")
    private String toWarehouseCode;
    @ApiModelProperty(value = "目标仓库名称")
    private String toWarehouseName;
    @ApiModelProperty(value = "发出货位ID")
    private String fromLocatorId;
    @ApiModelProperty(value = "发出货位编码")
    private String fromLocatorCode;
    @ApiModelProperty(value = "发出货位名称")
    private String fromLocatorName;
    @ApiModelProperty(value = "目标货位ID")
    private String toLocatorId;
    @ApiModelProperty(value = "目标货位编码")
    private String toLocatorCode;
    @ApiModelProperty(value = "目标货位名称")
    private String toLocatorName;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "单位CODE")
    private String uomCode;
    @ApiModelProperty(value = "单位名称")
    private String uomName;
    @ApiModelProperty(value = "条码个数")
    private int codeQty;
    @ApiModelProperty(value = "超发设置")
    @LovValue(value = "WMS.EXCESS_SETTING", meaningField = "excessSettingMeaning")
    private String excessSetting;
    @ApiModelProperty(value = "超发设置含义")
    private String excessSettingMeaning;
    @ApiModelProperty(value = "超发值")
    private String excessValue;
    @ApiModelProperty(value = "推荐货位")
    private String recommendLocatorCode;
}
