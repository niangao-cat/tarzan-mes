package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ExcelSheet(zh = "条码查询数据")
public class WmsMaterialLotQryExportResultDTO implements Serializable {
    private static final long serialVersionUID = 2220144708601984441L;

    @ApiModelProperty(value = "条码号")
    @ExcelColumn(zh = "条码号",order = 1)
    private String materialLotCode;

    @ApiModelProperty(value = "物料号")
    @ExcelColumn(zh = "物料编码",order = 2)
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    @ExcelColumn(zh = "物料描述",order = 3)
    private String materialName;

    @ApiModelProperty(value = "物料版本含义")
    private String materialVersionMeaning;

    @ApiModelProperty(value = "创建时间")
    @ExcelColumn(zh = "创建时间",order = 48)
    private String createDate;

    @ApiModelProperty(value = "是否有效meaning")
    @ExcelColumn(zh = "是否有效",order = 5)
    private String enableFlagMeaning;

    @ApiModelProperty(value = "状态含义")
    @ExcelColumn(zh = "状态",order = 10)
    private String statusMeaning;

    @ApiModelProperty(value = "数量")
    @ExcelColumn(zh = "数量",order = 6)
    private BigDecimal primaryUomQty;

    @ApiModelProperty(value = "单位")
    private String primaryUomCode;

    @ApiModelProperty(value = "批次号")
    @ExcelColumn(zh = "批次",order = 8)
    private String lot;

    @ApiModelProperty(value = "质量状态含义")
    @ExcelColumn(zh = "质量状态",order = 11)
    private String qualityStatusMeaning;

    @ApiModelProperty(value = "性能等级名称")
    @ExcelColumn(zh = "性能等级",order = 12)
    private String performanceLevelName;

    @ApiModelProperty(value = "工厂名称")
    @ExcelColumn(zh = "工厂",order = 13)
    private String siteName;

    @ApiModelProperty(value = "货位")
    @ExcelColumn(zh = "货位",order = 15)
    private String locatorCode;

    @ApiModelProperty(value = "仓库编码")
    @ExcelColumn(zh = "仓库",order = 14)
    private String wareHouseCode;

    @ApiModelProperty(value = "实际存储货位编码")
    @ExcelColumn(zh = "实际存储货位",order = 16)
    private String actualLocatorCode;

    @ApiModelProperty(value = "生产日期")
    @ExcelColumn(zh = "生产日期",order = 25)
    private String productDate;

    @ApiModelProperty(value = "有效期")
    @ExcelColumn(zh = "有效期",order = 26)
    private String effectiveDate;

    @ApiModelProperty(value = "销售订单头号")
    @ExcelColumn(zh = "销售订单号",order = 17)
    private String soNum;

    @ApiModelProperty(value = "销售订单行号")
    @ExcelColumn(zh = "销售订单行号",order = 18)
    private String soLineNum;

    @ApiModelProperty(value = "采购订单号")
    @ExcelColumn(zh = "采购订单号",order = 19)
    private String poNum;

    @ApiModelProperty(value = "采购订单行号")
    @ExcelColumn(zh = "采购订单行号",order = 20)
    private String poLineNum;

    @ApiModelProperty(value = "送货单号")
    @ExcelColumn(zh = "送货单号",order = 21)
    private String deliveryNum;

    @ApiModelProperty(value = "送货单行号")
    @ExcelColumn(zh = "送货单行号",order = 22)
    private String deliveryLineNum;

    @ApiModelProperty(value = "供应商号")
    @ExcelColumn(zh = "供应商编码",order = 23)
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    @ExcelColumn(zh = "供应商描述",order = 24)
    private String supplierName;

    @ApiModelProperty(value = "供应商批次")
    @ExcelColumn(zh = "供应商批次",order = 9)
    private String supplierLot;

    @ApiModelProperty(value = "入库时间")
    private Date inLocatorTime;

    @ExcelColumn(zh = "入库时间",order = 29)
    private String inLocatorTimeStr;

    @ApiModelProperty(value = "容器条码")
    @ExcelColumn(zh = "容器条码",order = 32)
    private String containerCode;

    @ApiModelProperty(value = "原始条码")
    @ExcelColumn(zh = "原始条码",order = 31)
    private String originalCode;

    @ApiModelProperty(value = "启用时间")
    @ExcelColumn(zh = "启用时间",order = 27)
    private String enableDate;

    @ApiModelProperty(value = "截止时间")
    @ExcelColumn(zh = "截止时间",order = 28)
    private String deadlineDate;

    @ApiModelProperty(value = "当前WKC")
    @ExcelColumn(zh = "当前WKC",order = 34)
    private String currentWck;

    @ApiModelProperty(value = "最后加工WKC")
    @ExcelColumn(zh = "最后加工WKC",order = 35)
    private String finalProcessWck;

    @ApiModelProperty(value = "返修标识")
    @LovValue(value = "Z_MTLOT_ENABLE_FLAG", meaningField = "reworkFlagMeaning")
    private String reworkFlag;

    @ApiModelProperty(value = "返修标识含义")
    @ExcelColumn(zh = "返修标识",order = 37)
    private String reworkFlagMeaning;

    @ApiModelProperty(value = "EO编码")
    @ExcelColumn(zh = "EO编码",order = 33)
    private String eoNum;

    @ApiModelProperty(value = "在制品标识")
    @ExcelColumn(zh = "在制品标识",order = 42)
    private String mfFlagMeaning;

    @ApiModelProperty(value = "创建人")
    @ExcelColumn(zh = "创建人",order = 47)
    private String createBy;

    @ApiModelProperty(value = "最后更新时间")
    @ExcelColumn(zh = "最后更新时间",order = 50)
    private String lastUpdateDate;

    @ApiModelProperty(value = "最后更新人")
    @ExcelColumn(zh = "最后更新人",order = 49)
    private String lastUpdateBy;

    @ApiModelProperty(value = "条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "状态")
    @LovValue(value = "MT.MTLOT.STATUS", meaningField = "statusMeaning")
    private String status;

    @ApiModelProperty(value = "是否有效")
    @LovValue(value = "Z_MTLOT_ENABLE_FLAG", meaningField = "enableFlagMeaning")
    private String enableFlag;

    @ApiModelProperty(value = "质量状态")
    @LovValue(value = "MT.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;

    @ApiModelProperty(value = "关联物料")
    private String materialId;

    @ApiModelProperty(value = "单位名称")
    @ExcelColumn(zh = "单位",order = 7)
    private String primaryUomName;

    @ApiModelProperty(value = "工厂ID")
    private String siteId;

    @ApiModelProperty(value = "工厂")
    private String siteCode;

    @ApiModelProperty(value = "货位Id")
    private String locatorId;

    @ApiModelProperty(value = "货位名称")
    private String locatorName;

    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;

    @ApiModelProperty(value = "仓库")
    private String wareHouse;

    @ApiModelProperty(value = "超期检验日期")
    private String overdueInspectionDate;

    @ApiModelProperty(value = "工单发料日期")
    private String woIssueDate;

    @ApiModelProperty(value = "色温")
    private String colorBin;

    @ApiModelProperty(value = "亮度")
    private String lightBin;

    @ApiModelProperty(value = "电压")
    private String voltageBin;

    @ApiModelProperty(value = "采购订单发运行号")
    private String poLineLocationNum;

    @ApiModelProperty(value = "打印次数")
    private String printTime;

    @ApiModelProperty(value = "打印原因")
    private String printReason;

    @ApiModelProperty(value = "湿敏等级")
    private String msl;

    @ApiModelProperty(value = "印字内容")
    private String printing;

    @ApiModelProperty(value = "膨胀系数")
    private String expansionCoefficients;

    @ApiModelProperty(value = "指令ID")
    private String instructionId;

    @ApiModelProperty(value = "不干胶号")
    private String stickerNumber;

    @ApiModelProperty(value = "转型物料")
    private String performanceLevel;

    @ApiModelProperty(value = "物料版本编码")
    @ExcelColumn(zh = "物料版本",order = 4)
    private String materialVersion;

    @ApiModelProperty(value = "实际存储货位")
    private String actualLocatorName;

    @ApiModelProperty(value = "在制品标识")
    private String mfFlag;

    @ApiModelProperty(value = "外箱条码")
    @ExcelColumn(zh = "外箱条码", order = 30)
    private String outMaterialLotCode;

    @ApiModelProperty(value = "料废调换标识")
    @LovValue(value = "Z_MTLOT_ENABLE_FLAG", meaningField = "replacementFlagMeaning")
    private String replacementFlag;

    @ApiModelProperty(value = "料废调换标识含义")
    @ExcelColumn(zh = "料废调换标识", order = 44)
    private String replacementFlagMeaning;

    @ApiModelProperty(value = "SAP账务标识")
    @LovValue(value = "Z_MTLOT_ENABLE_FLAG", meaningField = "sapAccountFlagMeaning")
    private String sapAccountFlag;

    @ApiModelProperty(value = "SAP账务标识含义")
    @ExcelColumn(zh = "SAP账务标识", order = 46)
    private String sapAccountFlagMeaning;

    @ApiModelProperty(value = "盘点停用标识含义")
    @ExcelColumn(zh = "盘点停用标识", order = 45)
    private String stocktakeFlagMeaning;

    @ApiModelProperty(value = "盘点停用标识")
    @LovValue(value = "Z_MTLOT_ENABLE_FLAG", meaningField = "stocktakeFlagMeaning")
    private String stocktakeFlag;

    @ApiModelProperty(value = "实验代码")
    @ExcelColumn(zh = "实验代码", order = 36)
    private String labCode;

    @ApiModelProperty(value = "冻结标识")
    @LovValue(value = "Z_MTLOT_ENABLE_FLAG", meaningField = "freezeFlagMeaning")
    private String freezeFlag;

    @ApiModelProperty(value = "冻结标识含义")
    @ExcelColumn(zh = "冻结标识",order = 43)
    private String freezeFlagMeaning;

    @ApiModelProperty(value = "指定工艺路线返修标识")
    @LovValue(value = "Z_MTLOT_ENABLE_FLAG", meaningField = "designedReworkFlagMeaning")
    private String designedReworkFlag;

    @ApiModelProperty(value = "指定工艺路线返修标识含义")
    @ExcelColumn(zh = "指定工艺路线返修标识",order = 38)
    private String designedReworkFlagMeaning;

    @ApiModelProperty(value = "返修工艺路线")
    private String reworkRouterId;

    @ApiModelProperty(value = "指定返修工艺路线")
    @ExcelColumn(zh = "指定返修工艺路线",order = 39)
    private String reworkRouterName;

    @ApiModelProperty(value = "返修工艺路线描述")
    @ExcelColumn(zh = "返修工艺路线描述",order = 40)
    private String reworkRouterDesc;

    @ApiModelProperty(value = "返修工艺路线版本")
    @ExcelColumn(zh = "返修工艺路线版本",order = 41)
    private String reworkRouterVersion;

}
