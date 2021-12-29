package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2021/3/23 11:11
 */
@Data
public class HmeEquipmentHisVO3 {

    @ApiModelProperty(value = "设备历史id")
    private String equipmentHisId;
    @ApiModelProperty(value = "设备类型")
    @LovValue(lovCode = "HME.EQUIPEMNT_TYPE", meaningField = "equipmentTypeMeaning")
    private String equipmentType;
    @ApiModelProperty(value = "设备类型含义")
    private String equipmentTypeMeaning;
    @ApiModelProperty(value = "资产编码")
    private String assetEncoding;
    @ApiModelProperty(value = "资产名称")
    private String assetName;
    @ApiModelProperty(value = "机身序号")
    private String equipmentBodyNum;
    @ApiModelProperty(value = "型号")
    private String model;
    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "销售商")
    private String supplier;
    @ApiModelProperty(value = "保管部门")
    private String businessName;
    @ApiModelProperty(value = "保管人")
    private String preserver;
    @ApiModelProperty(value = "存放地点")
    private String location;
    @ApiModelProperty(value = "使用频率")
    @LovValue(lovCode = "HME.USE_FREQUENCY", meaningField = "frequencyMeaning")
    private String frequency;
    @ApiModelProperty(value = "使用频率含义")
    private String frequencyMeaning;
    @ApiModelProperty(value = "单位")
    private String unit;
    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;
    @ApiModelProperty(value = "资产类别")
    @LovValue(lovCode = "HME.ASSET_CLASS", meaningField = "assetClassMeaning")
    private String assetClass;
    @ApiModelProperty(value = "资产类别含义")
    private String assetClassMeaning;
    @ApiModelProperty(value = "入账日期")
    private Date postingDate;
    @ApiModelProperty(value = "sap流水号")
    private String sapNum;
    @ApiModelProperty(value = "配置")
    private String equipmentConfig;
    @ApiModelProperty(value = "合同编号")
    private String contractNum;
    @ApiModelProperty(value = "募投")
    private String recruitement;
    @ApiModelProperty(value = "募投编号")
    private String recruitementNum;
    @ApiModelProperty(value = "OA验收单")
    private String oaCheckNum;
    @ApiModelProperty(value = "是否计量")
    @LovValue(lovCode = "HME.MEASURE_FLAG", meaningField = "measureFlagMeaning")
    private String measureFlag;
    @ApiModelProperty(value = "是否计量含义")
    private String measureFlagMeaning;
    @ApiModelProperty(value = "质保期到")
    private Date warrantyDate;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "点检分类")
    @LovValue(lovCode = "HME.EQUIPMENT_CATEGORY", meaningField = "equipmentCategoryMeaning")
    private String equipmentCategory;
    @ApiModelProperty(value = "点检分类含义")
    private String equipmentCategoryMeaning;
    @ApiModelProperty(value = "设备状态")
    @LovValue(lovCode = "HME.EQUIPMENT_STATUS", meaningField = "equipmentStatusMeaning")
    private String equipmentStatus;
    @ApiModelProperty(value = "设备状态含义")
    private String equipmentStatusMeaning;
    @ApiModelProperty(value = "币种")
    private String currency;
    @ApiModelProperty(value = "使用人")
    private String user;
    @ApiModelProperty(value = "处置单号")
    private String dealNum;
    @ApiModelProperty(value = "处置原因")
    private String dealReason;
    @ApiModelProperty(value = "归属权")
    private String belongTo;
    @ApiModelProperty(value = "应用类型")
    @LovValue(lovCode = "HME.APPLY_TYPE", meaningField = "applyTypeMeaning")
    private String applyType;
    @ApiModelProperty(value = "应用类型含义")
    private String applyTypeMeaning;
    @ApiModelProperty(value = "管理模式")
    @LovValue(lovCode = "HME.EQUIPMENT_MANAGE_MODEL", meaningField = "manageModeMeaning")
    private String manageMode;
    @ApiModelProperty(value = "管理模式含义")
    private String manageModeMeaning;
    @ApiModelProperty(value = "台账类别")
    @LovValue(lovCode = "HME.LEDGER_TYPE", meaningField = "ledgerCategoryMeaning")
    private String ledgerCategory;
    @ApiModelProperty(value = "台账类别含义")
    private String ledgerCategoryMeaning;
    @ApiModelProperty(value = "更新时间")
    private Date lastUpdateDate;
    @ApiModelProperty(value = "更新人")
    private String lastUpdatedBy;
    @ApiModelProperty(value = "更新人名称")
    private String lastUpdatedByName;

}
