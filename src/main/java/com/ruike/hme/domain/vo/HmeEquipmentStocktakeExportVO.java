package com.ruike.hme.domain.vo;

import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.util.Date;

/**
 * <p>
 * 设备盘点导出
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/20 14:24
 */
@Data
@ExcelSheet(zh = "设备盘点导出")
public class HmeEquipmentStocktakeExportVO {
    @ExcelColumn(zh = "盘点单号")
    private String stocktakeNum;

    @LovValue(lovCode = "HME_STOCKTAKE_STATUS", meaningField = "stocktakeStatusMeaning")
    private String stocktakeStatus;
    @ExcelColumn(zh = "单据状态")
    private String stocktakeStatusMeaning;

    @LovValue(lovCode = "HME_STOCKTAKE_TYPE", meaningField = "stocktakeTypeMeaning")
    private String stocktakeType;
    @ExcelColumn(zh = "盘点类型")
    private String stocktakeTypeMeaning;

    @ExcelColumn(zh = "盘点范围")
    private Integer stocktakeRange;

    @LovValue(lovCode = "HME.LEDGER_TYPE", meaningField = "ledgerTypeMeaning")
    private String ledgerType;
    @ExcelColumn(zh = "台账类型")
    private String ledgerTypeMeaning;

    @ExcelColumn(zh = "保管部门")
    private String businessName;

    @ExcelColumn(zh = "存放地点")
    private String location;

    @ExcelColumn(zh = "入账日期从", pattern = BaseConstants.Pattern.DATETIME)
    private Date postingDateFrom;

    @ExcelColumn(zh = "入账日期至", pattern = BaseConstants.Pattern.DATETIME)
    private Date postingDateTo;

    @ExcelColumn(zh = "单据备注")
    private String docRemark;

    @ExcelColumn(zh = "设备编码")
    private String assetEncoding;

    @ExcelColumn(zh = "设备名称")
    private String assetName;

    @ExcelColumn(zh = "设备保管部门")
    private String assetBusinessName;

    @LovValue(value = "HME.EQUIPMENT_STATUS", meaningField = "equipmentStatusMeaning")
    private String equipmentStatus;
    @ExcelColumn(zh = "设备状态")
    private String equipmentStatusMeaning;

    @LovValue(lovCode = "HME.USE_FREQUENCY", meaningField = "frequencyMeaning")
    private String frequency;

    @ExcelColumn(zh = "使用频率")
    private String frequencyMeaning;

    @ExcelColumn(zh = "盘点备注")
    private String remark;

    @LovValue(value = "HME_STOCKTAKE_FLAG", meaningField = "stocktakeFlagMeaning")
    private String stocktakeFlag;
    @ExcelColumn(zh = "盘点标识")
    private String stocktakeFlagMeaning;

    @ExcelColumn(zh = "盘点时间", pattern = BaseConstants.Pattern.DATETIME)
    private Date stocktakeDate;
}
