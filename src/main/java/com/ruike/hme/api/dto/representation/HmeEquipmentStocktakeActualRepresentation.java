package com.ruike.hme.api.dto.representation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 设备盘点实际 展现对象
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 15:14
 */
@Data
public class HmeEquipmentStocktakeActualRepresentation implements Serializable {
    private static final long serialVersionUID = 6928499504218325859L;

    @ApiModelProperty("序号")
    private Integer sequenceNum;
    @ApiModelProperty("主键")
    private String stocktakeActualId;
    @ApiModelProperty(value = "盘点单ID")
    private String stocktakeId;
    @ApiModelProperty(value = "设备ID")
    private String equipmentId;
    @ApiModelProperty(value = "盘点标识")
    @LovValue(value = "HME_STOCKTAKE_FLAG", meaningField = "stocktakeFlagMeaning")
    private String stocktakeFlag;
    @ApiModelProperty(value = "盘点标识含义")
    private String stocktakeFlagMeaning;
    @ApiModelProperty(value = "盘点时间")
    private Date stocktakeDate;
    @ApiModelProperty(value = "资产编码")
    private String assetEncoding;
    @ApiModelProperty(value = "资产名称")
    private String assetName;
    @ApiModelProperty(value = "资产类别")
    @LovValue(value = "HME.ASSET_CLASS", meaningField = "assetClassDes")
    private String assetClass;
    @ApiModelProperty(value = "设备描述")
    private String descriptions;
    @ApiModelProperty(value = "SAP流水号")
    private String sapNum;
    @ApiModelProperty(value = "机身序列号")
    private String equipmentBodyNum;
    @ApiModelProperty(value = "配置")
    private String equipmentConfig;
    @ApiModelProperty(value = "OA验收单号")
    private String oaCheckNum;
    @ApiModelProperty(value = "设备类别")
    @LovValue(value = "HME.EQUIPMENT_CATEGORY", meaningField = "equipmentCategoryDes")
    private String equipmentCategory;
    @ApiModelProperty(value = "设备类型")
    @LovValue(value = "HME.EQUIPEMNT_TYPE", meaningField = "equipmentTypeDes")
    private String equipmentType;
    @ApiModelProperty(value = "应用类型")
    @LovValue(value = "HME.APPLY_TYPE", meaningField = "applyTypeDes")
    private String applyType;
    @ApiModelProperty(value = "设备状态", required = true)
    @LovValue(value = "HME.EQUIPMENT_STATUS", meaningField = "equipmentStatusDes")
    private String equipmentStatus;
    @ApiModelProperty(value = "处置单号")
    private String dealNum;
    @ApiModelProperty(value = "处置原因")
    private String dealReason;
    @ApiModelProperty(value = "保管部门ID")
    private String businessId;
    @ApiModelProperty(value = "使用人")
    private String user;
    @ApiModelProperty(value = "保管人")
    private String preserver;
    @ApiModelProperty(value = "存放地点")
    private String location;
    @ApiModelProperty(value = "是否计量")
    @LovValue(value = "HME.MEASURE_FLAG", meaningField = "measureFlagDes")
    private String measureFlag;
    @ApiModelProperty(value = "使用频次")
    @LovValue(value = "HME.USE_FREQUENCY", meaningField = "frequencyDes")
    private String frequency;
    @ApiModelProperty(value = "归属权")
    private String belongTo;
    @ApiModelProperty(value = "入账日期")
    private Date postingDate;
    @ApiModelProperty(value = "销售商")
    private String supplier;
    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "型号")
    private String model;
    @ApiModelProperty(value = "单位")
    private String unit;
    @ApiModelProperty(value = "数量")
    private Long quantity;
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;
    @ApiModelProperty(value = "币种")
    @LovValue(value = "HME.CURRENCY", meaningField = "currencySymbol")
    private String currency;
    @ApiModelProperty(value = "合同编号")
    private String contractNum;
    @ApiModelProperty(value = "募投")
    private String recruitement;
    @ApiModelProperty(value = "募投编号")
    private String recruitementNum;
    @ApiModelProperty(value = "质保期")
    private Date warrantyDate;
    @ApiModelProperty(value = "组织ID")
    private String siteId;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty("入账日期从")
    private String postingDateStart;

    @ApiModelProperty("入账日期至")
    private String postingDateEnd;

    @ApiModelProperty(value = "资产类别描述")
    private String assetClassDes;

    @ApiModelProperty(value = "设备类别描述")
    private String equipmentCategoryDes;

    @ApiModelProperty(value = "设备类型描述")
    private String equipmentTypeDes;

    @ApiModelProperty(value = "应用类型描述")
    private String applyTypeDes;

    @ApiModelProperty(value = "设备状态描述")
    private String equipmentStatusDes;

    @ApiModelProperty(value = "是否计量描述")
    private String measureFlagDes;

    @ApiModelProperty(value = "使用频次描述")
    private String frequencyDes;

    @ApiModelProperty(value = "币种")
    private String currencySymbol;

    @ApiModelProperty(value = "组织编码")
    private String siteCode;

    @ApiModelProperty(value = "设备所在工位ID")
    private String stationId;

    @ApiModelProperty(value = "当前工位ID")
    private String workcellCodeId;

    @ApiModelProperty(value = "当前工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "当前工位")
    private String workcellName;

    @ApiModelProperty(value = "管理模式")
    @LovValue(value = "HME.EQUIPMENT_MANAGE_MODEL", meaningField = "attribute1Meaning")
    private String attribute1;

    @ApiModelProperty(value = "管理模式含义")
    private String attribute1Meaning;

    @ApiModelProperty(value = "保管部门名称")
    private String businessName;

    @ApiModelProperty(value = "台账类别")
    @LovValue(value = "HME.LEDGER_TYPE", meaningField = "ledgerTypeMeaning")
    private String ledgerType;

    @ApiModelProperty(value = "台账类别含义")
    private String ledgerTypeMeaning;
}
