package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/9 18:06
 */
@Data
@ExcelSheet(zh = "设备点检保养项目")
public class HmeEquipmentTagVO2 implements Serializable {

    private static final long serialVersionUID = -5189961078252358482L;

    @ApiModelProperty(value = "设备类别")
    @LovValue(lovCode = "HME.EQUIPMENT_CATEGORY", meaningField = "equipmentCategoryMeaning")
    private String equipmentCategory;
    @ApiModelProperty(value = "设备类别含义")
    @ExcelColumn(zh = "设备类别", order = 1)
    private String equipmentCategoryMeaning;
    @ApiModelProperty(value = "部门")
    private String businessId;
    @ApiModelProperty(value = "部门描述")
    @ExcelColumn(zh = "部门", order = 2)
    private String businessName;
    @ApiModelProperty(value = "工艺编码")
    @ExcelColumn(zh = "工艺编码", order = 3)
    private String operationName;
    @ApiModelProperty(value = "工艺Id")
    private String operationId;
    @ApiModelProperty(value = "数据组编码")
    @ExcelColumn(zh = "数据组编码", order = 4)
    private String tagGroupCode;
    @ApiModelProperty(value = "设备组描述")
    @ExcelColumn(zh = "设备组描述", order = 5)
    private String tagGroupDescription;
    @ApiModelProperty(value = "数据组Id")
    private String tagGroupId;
    @ApiModelProperty(value = "设备管理类型")
    @LovValue(lovCode = "HME.EQUIPMENT_MANAGE_TYPE", meaningField = "manageTypeMeaning")
    private String manageType;
    @ApiModelProperty(value = "设备管理类型含义")
    @ExcelColumn(zh = "设备管理类型", order = 6)
    private String manageTypeMeaning;
    @ApiModelProperty(value = "状态")
    @LovValue(lovCode = "HME.EQUIPMENT_MANAGE_STATUS", meaningField = "statusMeaning")
    private String status;
    @ApiModelProperty(value = "状态")
    @ExcelColumn(zh = "状态", order = 7)
    private String statusMeaning;
    @ApiModelProperty(value = "是否有效")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;
    @ApiModelProperty(value = "是否有效")
    @ExcelColumn(zh = "是否有效", order = 8)
    private String enableFlagMeaning;
    @ApiModelProperty(value = "排序")
    @ExcelColumn(zh = "排序", order = 9)
    private Long serialNumber;
    @ApiModelProperty(value = "项目编码")
    @ExcelColumn(zh = "项目编码", order = 10)
    private String tagCode;
    @ApiModelProperty(value = "项目描述")
    @ExcelColumn(zh = "项目描述", order = 11)
    private String tagDescription;
    @ApiModelProperty(value = "项目Id")
    private String tagId;
    @ApiModelProperty(value = "数据类型")
    private String valueType;
    @ApiModelProperty(value = "数据类型含义")
    @ExcelColumn(zh = "数据类型", order = 12)
    private String valueTypeMeaning;
    @ApiModelProperty(value = "收集方式")
    private String collectionMethod;
    @ApiModelProperty(value = "收集方式含义")
    @ExcelColumn(zh = "收集方式", order = 13)
    private String collectionMethodMeaning;
    @ApiModelProperty(value = "周期")
    @LovValue(lovCode = "  HME.EQUIPMENT_MANAGE_CYCLE", meaningField = "manageCycleMeaning")
    private String manageCycle;
    @ApiModelProperty(value = "周期")
    @ExcelColumn(zh = "周期", order = 14)
    private String manageCycleMeaning;
    @ApiModelProperty(value = "精度")
    @ExcelColumn(zh = "精度", order = 15)
    private BigDecimal accuracy;
    @ApiModelProperty(value = "最小值")
    @ExcelColumn(zh = "精度", order = 16)
    private BigDecimal minimumValue;
    @ApiModelProperty(value = "标准值")
    @ExcelColumn(zh = "标准值", order = 17)
    private BigDecimal standardValue;
    @ApiModelProperty(value = "最大值")
    @ExcelColumn(zh = "最大值", order = 18)
    private BigDecimal maximalValue;
    @ApiModelProperty(value = "单位")
    @ExcelColumn(zh = "单位", order = 19)
    private String uomName;
    @ApiModelProperty(value = "单位Id")
    private String uomId;
    @ApiModelProperty(value = "符合值")
    @ExcelColumn(zh = "符合值", order = 20)
    private String trueValue;
    @ApiModelProperty(value = "不符合值")
    @ExcelColumn(zh = "不符合值", order = 21)
    private String falseValue;
    @ApiModelProperty(value = "备注")
    @ExcelColumn(zh = "备注", order = 22)
    private String remark;
    @ApiModelProperty(value = "有效性")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "lineEnableFlagMeaning")
    private String lineEnableFlag;
    @ApiModelProperty(value = "有效性")
    @ExcelColumn(zh = "有效性", order = 23)
    private String lineEnableFlagMeaning;
}
