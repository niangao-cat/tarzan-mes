package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-05-18 12:59
 */
@Data
public class WmsMaterialExchangeDocLineDTO {

    @ApiModelProperty(value = "单据行ID")
    private String instructionId;
    @ApiModelProperty(value = "单据行号")
    private String instructionLineNum;
    @ApiModelProperty(value = "单据行编码")
    private String instructionNum;
    @ApiModelProperty(value = "单据行状态")
    @LovValue(lovCode = "WMS.SUPPLIER_EXCHANGE_DOC_LINE.STATUS",meaningField="instructionStatusMeaning",defaultMeaning = "无")
    private String instructionStatus;
    @ApiModelProperty(value = "单据行状态说明")
    private String instructionStatusMeaning;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "调换数量")
    private BigDecimal quantity;
    @ApiModelProperty(value = "已发出数量")
    private BigDecimal executeQty;
    @ApiModelProperty(value = "发出数量")
    private BigDecimal addQty;
    @ApiModelProperty(value = "执行数量")
    private BigDecimal actualQty;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "单位名称")
    private String uomName;
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;
    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;
    @ApiModelProperty(value = "货位ID")
    private String locatorId;
    @ApiModelProperty(value = "货位编码")
    private String locatorCode;
    @ApiModelProperty(value = "货位名称")
    private String locatorName;
    @ApiModelProperty(value = "备注")
    private String remark;
}
