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
public class WmsCostCtrMaterialDTO9 {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "单据行ID")
    private String instructionId;
    @ApiModelProperty(value = "单据行编码")
    private String instructionNum;
    @ApiModelProperty(value = "单据行状态")
    @LovValue(lovCode = "WMS.COST_CENTER_DOCUMENT_LINE.STATUS",meaningField="lineStatusMeaning",defaultMeaning = "无")
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
    @ApiModelProperty(value = "执行数量")
    private BigDecimal executeQty;
    @ApiModelProperty(value = "发出仓库ID")
    private String toWarehouseId;
    @ApiModelProperty(value = "发出仓库编码")
    private String toWarehouseCode;
    @ApiModelProperty(value = "发出仓库名称")
    private String toWarehouseName;
    @ApiModelProperty(value = "发出货位ID")
    private String toLocatorId;
    @ApiModelProperty(value = "发出货位编码")
    private String toLocatorCode;
    @ApiModelProperty(value = "发出货位名称")
    private String toLocatorName;

    private List<WmsCostCtrMaterialDTO3> barDtoList;

}
