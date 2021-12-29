package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.util.Date;

@Data
@ExcelSheet(zh = "条码查询历史数据")
public class WmsMaterialLotHisExportResultDTO implements Serializable {

    private static final long serialVersionUID = 1896606097590432547L;

    @ApiModelProperty(value = "条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "条码号")
    @ExcelColumn(zh = "条码号",order = 1)
    private String materialLotCode;

    @ApiModelProperty(value = "事件")
    @ExcelColumn(zh = "主表事件",order = 2)
    private String eventTypeDesc;

    @ApiModelProperty(value = "主表事件id")
    @ExcelColumn(zh = "主表事件id",order = 3)
    private String eventId;

    @ApiModelProperty(value = "主表事件创建时间")
    @ExcelColumn(zh = "主表事件创建时间",order = 4)
    private String eventTime;

    @ApiModelProperty(value = "主表事件创建人")
    @ExcelColumn(zh = "主表事件创建人",order = 5)
    private String eventBy;

    @ApiModelProperty(value = "扩展表事件")
    @ExcelColumn(zh = "扩展表事件",order = 6)
    private String attrEventTypeDesc;

    @ApiModelProperty(value = "扩展表事件id")
    @ExcelColumn(zh = "扩展表事件id",order = 7)
    private String attrEventId;

    @ApiModelProperty(value = "扩展表事件创建时间")
    @ExcelColumn(zh = "扩展表事件创建时间",order = 8)
    private String attrEventTime;

    @ApiModelProperty(value = "扩展表事件创建人")
    @ExcelColumn(zh = "扩展表事件创建人",order = 9)
    private String attrEventBy;

    @ApiModelProperty(value = "事务数量")
    @ExcelColumn(zh = "事务数量",order = 10)
    private Double trxPrimaryQty;

    @ApiModelProperty(value = "是否有效meaning")
    @ExcelColumn(zh = "是否有效",order = 11)
    private String enableFlagMeaning;

    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(zh = "物料编码",order = 12)
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    @ExcelColumn(zh = "物料描述",order = 13)
    private String materialName;

    @ApiModelProperty(value = "数量")
    @ExcelColumn(zh = "数量",order = 14)
    private Double primaryUomQty;

    @ApiModelProperty(value = "单位")
    private String primaryUomCode;

    @ApiModelProperty(value = "批次")
    @ExcelColumn(zh = "批次",order = 16)
    private String lot;

    @ApiModelProperty(value = "质量状态含义")
    @ExcelColumn(zh = "质量状态",order = 17)
    private String qualityStatusMeaning;

    @ApiModelProperty(value = "工厂名称")
    @ExcelColumn(zh = "工厂",order = 18)
    private String siteName;

    @ApiModelProperty(value = "仓库编码")
    @ExcelColumn(zh = "仓库",order = 19)
    private String wareHouseCode;

    @ApiModelProperty(value = "货位")
    @ExcelColumn(zh = "货位",order = 19)
    private String locatorCode;

    @ApiModelProperty(value = "供应商号")
    @ExcelColumn(zh = "供应商编码",order = 20)
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    @ExcelColumn(zh = "供应商描述",order = 21)
    private String supplierName;

    @ApiModelProperty(value = "入库时间")
    private Date inLocatorTime;

    @ExcelColumn(zh = "入库时间",order = 22)
    private String inLocatorTimeStr;

    @ApiModelProperty(value = "容器条码")
    @ExcelColumn(zh = "容器条码",order = 23)
    private String containerCode;

    @ApiModelProperty(value = "EO编码")
    @ExcelColumn(zh = "EO编码",order = 24)
    private String eoNum;

    @ApiModelProperty(value = "状态含义")
    @ExcelColumn(zh = "状态",order = 25)
    private String statusMeaning;

    @ApiModelProperty(value = "物料版本")
    @ExcelColumn(zh = "物料版本",order = 26)
    private String materialVersion;

    @ApiModelProperty(value = "实际存储货位编码")
    @ExcelColumn(zh = "实际存储货位",order = 26)
    private String actualLocatorCode;

    @ApiModelProperty(value = "生产日期")
    @ExcelColumn(zh = "生产日期",order = 27)
    private String productDate;

    @ApiModelProperty(value = "返修标志")
    @ExcelColumn(zh = "返修标志",order = 28)
    private String reworkFlag;

    @ApiModelProperty(value = "在制品标识含义")
    @ExcelColumn(zh = "在制品标识",order = 29)
    private String mfFlagMeaning;

    @ApiModelProperty(value = "当前WCK")
    @ExcelColumn(zh = "当前WCK",order = 30)
    private String currentWck;

    @ApiModelProperty(value = "最后加工WCK")
    @ExcelColumn(zh = "最后加工WCK",order = 31)
    private String finalProcessWck;

    @ApiModelProperty(value = "有效日期")
    @ExcelColumn(zh = "有效日期",order = 32)
    private String effectiveDate;

    @ApiModelProperty(value = "启用时间")
    @ExcelColumn(zh = "启用时间",order = 33)
    private String enableDate;

    @ApiModelProperty(value = "截止时间")
    @ExcelColumn(zh = "截止时间",order = 34)
    private String deadlineDate;

    @ApiModelProperty(value = "原始条码")
    @ExcelColumn(zh = "原始条码",order = 35)
    private String originalCode;

    @ApiModelProperty(value = "销售订单头号")
    @ExcelColumn(zh = "销售订单号",order = 36)
    private String soNum;

    @ApiModelProperty(value = "销售订单行号")
    @ExcelColumn(zh = "销售订单行号",order = 37)
    private String soLineNum;

    @ApiModelProperty(value = "采购订单号")
    @ExcelColumn(zh = "采购订单号",order = 38)
    private String poNum;

    @ApiModelProperty(value = "采购订单行号")
    @ExcelColumn(zh = "采购订单行号",order = 39)
    private String poLineNum;

    @ApiModelProperty(value = "送货单号")
    @ExcelColumn(zh = "送货单号",order = 40)
    private String deliveryNum;

    @ApiModelProperty(value = "送货单行号")
    @ExcelColumn(zh = "送货单行号",order = 41)
    private String deliveryLineNum;

    @ApiModelProperty(value = "性能等级")
    @ExcelColumn(zh = "性能等级",order = 42)
    private String performanceLevel;

    @ApiModelProperty(value = "状态")
    @LovValue(value = "MT.MTLOT.STATUS", meaningField = "statusMeaning")
    private String status;

    @ApiModelProperty(value = "质量状态")
    @LovValue(value = "MT.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;

    @ApiModelProperty(value = "关联物料")
    private String materialId;

    @ApiModelProperty(value = "单位名称")
    @ExcelColumn(zh = "单位",order = 15)
    private String primaryUomName;

    @ApiModelProperty(value = "工厂ID")
    private String siteId;

    @ApiModelProperty(value = "工厂")
    private String siteCode;

    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;

    @ApiModelProperty(value = "仓库")
    private String wareHouse;

    @ApiModelProperty(value = "货位名称")
    private String locatorName;

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

    @ApiModelProperty(value = "原始条码id")
    private String originalId;

    @ApiModelProperty(value = "打印次数")
    private String printTime;

    @ApiModelProperty(value = "打印原因")
    private String printReason;

    @ApiModelProperty(value = "创建时间")
    private String creationDate;

    @ApiModelProperty(value = "创建人")
    private String createdBy;

    @ApiModelProperty(value = "最后更新时间")
    private String LastUpdateDate;

    @ApiModelProperty(value = "最后更新人")
    private String LastUpdatedBy;

    @ApiModelProperty(value = "是否有效")
    @LovValue(value = "Z_MTLOT_ENABLE_FLAG", meaningField = "enableFlagMeaning")
    private String enableFlag;

    @ApiModelProperty(value = "湿敏等级")
    private String msl;

    @ApiModelProperty(value = "印字内容")
    private String printing;

    @ApiModelProperty(value = "印字内容")
    private String expansionCoefficients;

    @ApiModelProperty(value = "指令ID")
    private String instructionId;

    @ApiModelProperty(value = "不干胶号")
    private String stickerNumber;

    @ApiModelProperty(value = "性能等级名称")
    private String performanceLevelName;

    @ApiModelProperty(value = "物料版本")
    private String materialVersionMeaning;

    @ApiModelProperty(value = "实际存储货位")
    private String actualLocatorName;

    @ApiModelProperty(value = "在制品标识")
    private String mfFlag;

}
