package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMaterialLotHisResultDTO implements Serializable {

    @ApiModelProperty(value = "条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "条码号")
    private String materialLotCode;

    @ApiModelProperty(value = "状态")
    @LovValue(value = "MT.MTLOT.STATUS", meaningField = "statusMeaning")
    private String status;

    @ApiModelProperty(value = "状态含义")
    private String statusMeaning;

    @ApiModelProperty(value = "质量状态")
    @LovValue(value = "MT.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;

    @ApiModelProperty(value = "质量状态含义")
    private String qualityStatusMeaning;

    @ApiModelProperty(value = "关联物料")
    private String materialId;

    @ApiModelProperty(value = "物料号")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "批次号")
    private String lot;

    @ApiModelProperty(value = "数量")
    private Double primaryUomQty;

    @ApiModelProperty(value = "单位")
    private String primaryUomCode;

    @ApiModelProperty(value = "单位名称")
    private String primaryUomName;

    @ApiModelProperty(value = "工厂ID")
    private String siteId;

    @ApiModelProperty(value = "工厂")
    private String siteCode;

    @ApiModelProperty(value = "工厂名称")
    private String siteName;

    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;

    @ApiModelProperty(value = "仓库编码")
    private String wareHouseCode;

    @ApiModelProperty(value = "仓库")
    private String wareHouse;

    @ApiModelProperty(value = "货位")
    private String locatorCode;

    @ApiModelProperty(value = "货位名称")
    private String locatorName;

    @ApiModelProperty(value = "生产日期")
    private String productDate;

    @ApiModelProperty(value = "有效日期")
    private String effectiveDate;

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

    @ApiModelProperty(value = "采购订单号")
    private String poNum;

    @ApiModelProperty(value = "采购订单行号")
    private String poLineNum;

    @ApiModelProperty(value = "采购订单发运行号")
    private String poLineLocationNum;

    @ApiModelProperty(value = "销售订单头号")
    private String soNum;

    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;

    @ApiModelProperty(value = "供应商号")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "原始条码")
    private String originalCode;

    @ApiModelProperty(value = "原始条码id")
    private String originalId;

    @ApiModelProperty(value = "容器条码")
    private String containerCode;

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

    @ApiModelProperty(value = "是否有效meaning")
    private String enableFlagMeaning;

    @ApiModelProperty(value = "事件")
    private String eventTypeDesc;

    @ApiModelProperty(value = "扩展字段事件")
    private String attrEventTypeDesc;

    @ApiModelProperty(value = "事件创建时间")
    private String eventTime;

    @ApiModelProperty(value = "事件创建人")
    private String eventBy;

    @ApiModelProperty(value = "扩展事件创建时间")
    private String attrEventTime;

    @ApiModelProperty(value = "扩展事件创建人")
    private String attrEventBy;

    @ApiModelProperty(value = "事务数量")
    private Double trxPrimaryQty;

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

    @ApiModelProperty(value = "入库时间")
    private Date inLocatorTime;

    @ApiModelProperty(value = "EO编码")
    private String eoNum;

    @ApiModelProperty(value = "启用时间")
    private String enableDate;

    @ApiModelProperty(value = "截止时间")
    private String deadlineDate;

    @ApiModelProperty(value = "当前WCK")
    private String currentWck;

    @ApiModelProperty(value = "最后加工WCK")
    private String finalProcessWck;

    @ApiModelProperty(value = "返修标记")
    private String reworkFlag;

    @ApiModelProperty(value = "性能等级")
    private String performanceLevel;

    @ApiModelProperty(value = "性能等级名称")
    private String performanceLevelName;

    @ApiModelProperty(value = "送货单号")
    private String deliveryNum;

    @ApiModelProperty(value = "送货单行号")
    private String deliveryLineNum;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "物料版本")
    private String materialVersionMeaning;

    @ApiModelProperty(value = "实际存储货位编码")
    private String actualLocatorCode;

    @ApiModelProperty(value = "实际存储货位")
    private String actualLocatorName;

    @ApiModelProperty(value = "在制品标识")
    private String mfFlag;

    @ApiModelProperty(value = "在制品标识含义")
    private String mfFlagMeaning;

    @ApiModelProperty(value = "事件id")
    private String eventId;

    @ApiModelProperty(value = "扩展表事件id")
    private String attrEventId;

    @ApiModelProperty(value = "盘点停用标识含义")
    private String stocktakeFlagMeaning;

    @ApiModelProperty(value = "盘点停用标识")
    @LovValue(value = "Z_MTLOT_ENABLE_FLAG", meaningField = "stocktakeFlagMeaning")
    private String stocktakeFlag;

    @ApiModelProperty(value = "料废调用标识")
    @LovValue(value = "Z_MTLOT_ENABLE_FLAG", meaningField = "replacementFlagMeaning")
    private String replacementFlag;

    @ApiModelProperty(value = "料废调换标识")
    private String replacementFlagMeaning;
}
