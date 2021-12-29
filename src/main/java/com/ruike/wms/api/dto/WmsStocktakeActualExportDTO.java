package com.ruike.wms.api.dto;

import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.math.BigDecimal;

/**
 * <p>
 * 盘点实际导出
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/19 19:37
 */
@ExcelSheet(title = "条码明细")
@Data
public class WmsStocktakeActualExportDTO {

    @ExcelColumn(title = "盘点单据编号")
    private String stocktakeNum;

    @ExcelColumn(title = "实物条码")
    private String materialLotCode;

    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "enableFlag")
    private String materialLotEnableFlag;
    @ExcelColumn(title = "是否有效")
    private String enableFlag;

    @LovValue(lovCode = "WMS.MTLOT.STATUS", meaningField = "materialLotStatusMeaning")
    private String materialLotStatus;

    @ExcelColumn(title = "条码状态")
    private String materialLotStatusMeaning;

    @ExcelColumn(title = "物料编码")
    private String materialCode;

    @ExcelColumn(title = "物料版本")
    private String materialVersion;

    @ExcelColumn(title = "物料描述")
    private String materialName;

    @ExcelColumn(title = "账面数量")
    private Double currentQuantity;

    @ExcelColumn(title = "初盘数量")
    private Double firstcountQuantity;

    @ExcelColumn(title = "复盘数量")
    private Double recountQuantity;

    @ExcelColumn(title = "差异数量")
    private BigDecimal differentQuantity;

    @ExcelColumn(title = "单位")
    private String uomCode;

    @ExcelColumn(title = "账面货位")
    private String locatorCode;

    @ExcelColumn(title = "初盘货位")
    private String firstcountLocatorCode;

    @ExcelColumn(title = "复盘货位")
    private String recountLocatorCode;

    @ExcelColumn(title = "容器条码")
    private String containerCode;

    @ExcelColumn(title = "批次")
    private String lotCode;

    @LovValue(lovCode = "WMS.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;
    @ExcelColumn(title = "质量状态")
    private String qualityStatusMeaning;

    @ExcelColumn(title = "初盘备注")
    private String firstcountRemark;

    @ExcelColumn(title = "初盘人")
    private String firstcountByName;

    @ExcelColumn(title = "初盘时间", pattern = BaseConstants.Pattern.DATETIME)
    private String firstcountDate;

    @ExcelColumn(title = "复盘备注")
    private String recountRemark;

    @ExcelColumn(title = "复盘人")
    private String recountByName;

    @ExcelColumn(title = "复盘时间", pattern = BaseConstants.Pattern.DATETIME)
    private String recountDate;
}
