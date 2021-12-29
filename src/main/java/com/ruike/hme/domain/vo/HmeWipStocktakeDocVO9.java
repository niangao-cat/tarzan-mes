package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeWipStocktakeDocVO9
 *
 * @author: chaonan.hu@hand-china.com 2021/4/8 13:45:12
 **/
@Data
@ExcelSheet(zh = "在制盘点明细")
public class HmeWipStocktakeDocVO9 implements Serializable {
    private static final long serialVersionUID = -6783454372812170842L;

    @ApiModelProperty(value = "盘点单ID")
    private String stocktakeId;

    @ApiModelProperty(value = "盘点单号")
    @ExcelColumn(zh = "盘点单号",order = 1)
    private String stocktakeNum;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "工单编号")
    @ExcelColumn(zh = "工单编号",order = 2)
    private String workOrderNum;

    @ApiModelProperty(value = "物料组")
    @ExcelColumn(zh = "物料组",order = 7)
    private String itemGroup;

    @ApiModelProperty(value = "BOM编码")
    private String bomName;

    @ApiModelProperty(value = "Bom描述")
    private String description;

    @ApiModelProperty("bom版本号")
    @ExcelColumn(zh = "BOM版本号",order = 3)
    private String bomProductionVersion;

    @ApiModelProperty("bom版本描述")
    @ExcelColumn(zh = "BOM版本描述",order = 4)
    private String bomProductionVersionDesc;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(zh = "物料编码",order = 5)
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    @ExcelColumn(zh = "物料名称",order = 6)
    private String materialName;

    @ApiModelProperty(value = "产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "产线编码")
    @ExcelColumn(zh = "产线编码",order = 8)
    private String prodLineCode;

    @ApiModelProperty(value = "产线名称")
    @ExcelColumn(zh = "产线名称",order = 9)
    private String prodLineName;

    @ApiModelProperty(value = "工序ID")
    private String workcellId;

    @ApiModelProperty(value = "工序编码")
    @ExcelColumn(zh = "工序编码",order = 10)
    private String workcellCode;

    @ApiModelProperty(value = "工序名称")
    @ExcelColumn(zh = "工序名称",order = 11)
    private String workcellName;

    @ApiModelProperty(value = "实物条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "实物条码")
    @ExcelColumn(zh = "实物条码",order = 12)
    private String materialLotCode;

    @ApiModelProperty(value = "返修标识")
    private String reworkFlag;

    @ApiModelProperty(value = "返修标识")
    @ExcelColumn(zh = "返修标识",order = 14)
    private String reworkFlagMeaning;

    @ApiModelProperty(value = "账面数量")
    @ExcelColumn(zh = "账面数量",order = 15)
    private BigDecimal currentQuantity;

    @ApiModelProperty(value = "单位ID")
    private String uomId;

    @ApiModelProperty(value = "单位")
    @ExcelColumn(zh = "单位",order = 16)
    private String uomCode;

    @ApiModelProperty(value = "初盘数量")
    @ExcelColumn(zh = "初盘数量",order = 17)
    private BigDecimal firstcountQuantity;

    @ApiModelProperty(value = "初盘产线ID")
    private String firstcountProdLineId;

    @ApiModelProperty(value = "初盘产线")
    @ExcelColumn(zh = "初盘产线",order = 18)
    private String firstcountProdLineCode;

    @ApiModelProperty(value = "初盘工序ID")
    private String firstcountWorkcellId;

    @ApiModelProperty(value = "初盘工序")
    @ExcelColumn(zh = "初盘工序",order = 19)
    private String firstcountWorkcellCode;

    @ApiModelProperty(value = "复盘数量")
    @ExcelColumn(zh = "复盘数量",order = 20)
    private BigDecimal recountQuantity;

    @ApiModelProperty(value = "复盘产线ID")
    private String recountProdLineId;

    @ApiModelProperty(value = "复盘产线")
    @ExcelColumn(zh = "复盘产线",order = 21)
    private String recountProdLineCode;

    @ApiModelProperty(value = "复盘工序ID")
    private String recountWorkcellId;

    @ApiModelProperty(value = "复盘工序")
    @ExcelColumn(zh = "复盘工序",order = 22)
    private String recountWorkcellCode;

    @ApiModelProperty(value = "初盘差异")
    @ExcelColumn(zh = "初盘差异",order = 23)
    private BigDecimal firstcountDiff;

    @ApiModelProperty(value = "复盘差异")
    @ExcelColumn(zh = "复盘差异",order = 24)
    private BigDecimal recountDiff;

    @ApiModelProperty(value = "初盘人ID")
    private Long firstcountBy;

    @ApiModelProperty(value = "初盘人")
    @ExcelColumn(zh = "初盘人",order = 25)
    private String firstcountByName;

    @ApiModelProperty(value = "初盘时间")
    @ExcelColumn(zh = "序号",order = 26)
    private String firstcountDate;

    @ApiModelProperty(value = "初盘备注")
    @ExcelColumn(zh = "初盘备注",order = 27)
    private String firstcountRemark;

    @ApiModelProperty(value = "复盘人ID")
    private Long recountBy;

    @ApiModelProperty(value = "复盘人")
    @ExcelColumn(zh = "复盘人",order = 28)
    private String recountByName;

    @ApiModelProperty(value = "复盘时间")
    @ExcelColumn(zh = "复盘时间",order = 29)
    private String recountDate;

    @ApiModelProperty(value = "复盘备注")
    @ExcelColumn(zh = "复盘备注",order = 30)
    private String recountRemark;

    @ApiModelProperty(value = "条码容器ID")
    private String currentContainerId;

    @ApiModelProperty(value = "条码容器")
    @ExcelColumn(zh = "条码容器",order = 13)
    private String currentContainerCode;

    @ApiModelProperty(value = "返修条码ID")
    private String repairMaterialLotId;

    @ApiModelProperty(value = "返修条码")
    @ExcelColumn(zh = "返修条码",order = 31)
    private String repairMaterialLotCode;
}
