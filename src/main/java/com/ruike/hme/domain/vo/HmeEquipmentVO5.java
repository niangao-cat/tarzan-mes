package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName HmeEquipmentVO5
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/15 15:45
 * @Version 1.0
 **/
@Data
@ExcelSheet(zh = "设备台账")
public class HmeEquipmentVO5 implements Serializable {

    private static final long serialVersionUID = -1084161130232911746L;

    @ApiModelProperty("序号")
    @ExcelColumn(zh = "序号",order = 0)
    private Long orderNum;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty(value = "设备Id")
    private String equipmentId;

    @ApiModelProperty(value = "资产编码")
    @ExcelColumn(zh = "资产编码",order = 2)
    private String assetEncoding;

    @ApiModelProperty(value = "资产名称")
    @ExcelColumn(zh = "资产名称",order = 3)
    private String assetName;

    @ApiModelProperty(value = "资产类别",required = true)
    @LovValue(value = "HME.ASSET_CLASS", meaningField = "assetClassDes")
    private String assetClass;

    @ApiModelProperty(value = "资产类别描述")
    @ExcelColumn(zh = "资产类别",order = 16)
    private String assetClassDes;

    @ApiModelProperty(value = "设备描述")
    private String descriptions;

    @ApiModelProperty(value = "SAP流水号")
    @ExcelColumn(zh = "SAP流水号",order = 18)
    private String sapNum;

    @ApiModelProperty(value = "机身序列号")
    @ExcelColumn(zh = "机身序列号",order = 4)
    private String equipmentBodyNum;

    @ApiModelProperty(value = "配置")
    @ExcelColumn(zh = "配置",order = 18)
    private String equipmentConfig;

    @ApiModelProperty(value = "OA验收单号")
    @ExcelColumn(zh = "OA验收单号",order = 23)
    private String oaCheckNum;

    @ApiModelProperty(value = "详细设备类别")
    @LovValue(value = "HME.EQUIPMENT_CATEGORY", meaningField = "equipmentCategoryDes")
    private String equipmentCategory;

    @ApiModelProperty(value = "详细设备类别描述")
    @ExcelColumn(zh = "详细设备类别",order = 27)
    private String equipmentCategoryDes;

    @ApiModelProperty(value = "设备类型")
    @LovValue(value = "HME.EQUIPEMNT_TYPE", meaningField = "equipmentTypeDes")
    private String equipmentType;

    @ApiModelProperty(value = "设备类型描述")
    @ExcelColumn(zh = "设备类型",order = 1)
    private String equipmentTypeDes;

    @ApiModelProperty(value = "应用类型")
    @LovValue(value = "HME.APPLY_TYPE", meaningField = "applyTypeDes")
    private String applyType;

    @ApiModelProperty(value = "应用类型描述")
    @ExcelColumn(zh = "应用类型",order = 31)
    private String applyTypeDes;

    @ApiModelProperty(value = "设备状态",required = true)
    @LovValue(value = "HME.EQUIPMENT_STATUS", meaningField = "equipmentStatusDes")
    private String equipmentStatus;

    @ApiModelProperty(value = "设备状态描述")
    @ExcelColumn(zh = "设备状态",order = 28)
    private String equipmentStatusDes;

    @ApiModelProperty(value = "处置单号")
    @ExcelColumn(zh = "处置单号",order = 34)
    private String dealNum;

    @ApiModelProperty(value = "处置原因")
    @ExcelColumn(zh = "处置原因",order = 35)
    private String dealReason;

    @ApiModelProperty(value = "保管部门ID")
    private String businessId;

    @ApiModelProperty(value = "使用人")
    @ExcelColumn(zh = "使用人",order = 33)
    private String user;

    @ApiModelProperty(value = "保管人")
    @ExcelColumn(zh = "保管人",order = 9)
    private String preserver;

    @ApiModelProperty(value = "存放地点")
    @ExcelColumn(zh = "存放地点",order = 10)
    private String location;

    @ApiModelProperty(value = "是否计量")
    @LovValue(value = "HME.MEASURE_FLAG", meaningField = "measureFlagDes")
    private String measureFlag;

    @ApiModelProperty(value = "是否计量描述")
    @ExcelColumn(zh = "是否计量",order = 24)
    private String measureFlagDes;

    @ApiModelProperty(value = "使用频次")
    @LovValue(value = "HME.USE_FREQUENCY", meaningField = "frequencyDes")
    private String frequency;

    @ApiModelProperty(value = "使用频次描述")
    @ExcelColumn(zh = "使用频次",order = 11)
    private String frequencyDes;

    @ApiModelProperty(value = "归属权" )
    private String belongTo;

    @ApiModelProperty(value = "入账日期")
    private Date postingDate;

    @ApiModelProperty(value = "入账日期字符串")
    @ExcelColumn(zh = "入账日期",order = 17)
    private String postingDateStr;

    @ApiModelProperty(value = "销售商")
    @ExcelColumn(zh = "销售商",order = 7)
    private String supplier;

    @ApiModelProperty(value = "品牌")
    @ExcelColumn(zh = "品牌",order = 6)
    private String brand;

    @ApiModelProperty(value = "型号")
    @ExcelColumn(zh = "型号",order = 5)
    private String model;

    @ApiModelProperty(value = "单位")
    @ExcelColumn(zh = "单位",order = 12)
    private String unit;

    @ApiModelProperty(value = "数量")
    @ExcelColumn(zh = "数量",order = 13)
    private Long quantity;

    @ApiModelProperty(value = "金额")
    @ExcelColumn(zh = "金额",order = 14)
    private BigDecimal amount;

    @ApiModelProperty(value = "币种")
    @LovValue(value = "HME.CURRENCY", meaningField = "currencySymbol")
    private String currency;

    @ApiModelProperty(value = "币种")
    @ExcelColumn(zh = "币种",order = 15)
    private String currencySymbol;

    @ApiModelProperty(value = "合同编号")
    @ExcelColumn(zh = "合同编号",order = 20)
    private String contractNum;

    @ApiModelProperty(value = "募投")
    @ExcelColumn(zh = "募投",order = 21)
    private String recruitement;

    @ApiModelProperty(value = "募投编号")
    @ExcelColumn(zh = "募投编号",order = 22)
    private String recruitementNum;

    @ApiModelProperty(value = "质保期")
    private Date warrantyDate;

    @ApiModelProperty(value = "质保期字符串")
    @ExcelColumn(zh = "质保期",order = 25)
    private String warrantyDateStr;

    @ApiModelProperty(value = "组织ID")
    private String siteId;

    @ApiModelProperty(value = "组织编码")
    private String siteCode;

    @ApiModelProperty(value = "备注")
    @ExcelColumn(zh = "备注",order = 26)
    private String remark;

    @ApiModelProperty(value = "设备所在工位ID")
    private String stationId;

    @ApiModelProperty(value = "当前工位编码")
    @ExcelColumn(zh = "当前工位编码",order = 29)
    private String workcellCode;

    @ApiModelProperty(value = "当前工位")
    @ExcelColumn(zh = "当前工位描述",order = 30)
    private String workcellName;

    @ApiModelProperty(value = "管理模式")
    @LovValue(value = "HME.EQUIPMENT_MANAGE_MODEL", meaningField = "attribute1Meaning")
    private String attribute1;

    @ApiModelProperty(value = "管理模式含义")
    @ExcelColumn(zh = "管理模式",order = 32)
    private String attribute1Meaning;

    @ApiModelProperty(value = "保管部门名称")
    @ExcelColumn(zh = "保管部门",order = 8)
    private String businessName;

    @ApiModelProperty(value = "台账类别")
    @LovValue(value = "HME.LEDGER_TYPE", meaningField = "ledgerTypeMeaning")
    private String ledgerType;

    @ApiModelProperty(value = "台账类别含义")
    @ExcelColumn(zh = "台账类别",order = 37)
    private String ledgerTypeMeaning;
}
