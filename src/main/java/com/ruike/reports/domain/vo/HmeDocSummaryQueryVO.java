package com.ruike.reports.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName HmeDocSummaryQueryVO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/2/25 16:27
 * @Version 1.0
 **/
@Data
public class HmeDocSummaryQueryVO implements Serializable {
    private static final long serialVersionUID = 8454318175347797379L;

    @ApiModelProperty("单据类型编码")
    @LovValue(value = "HME.INSTRUCTION_DOC_TYPE", meaningField = "instructionDocTypeMeaning")
    private String instructionDocType;

    @ApiModelProperty("单据类型含义")
    private String instructionDocTypeMeaning;

    @ApiModelProperty("单据号")
    private String instructionDocNum;

    @ApiModelProperty("单据状态")
    @LovValue(value = "HME.INSTRUCTION_DOC_STATUS", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;

    @ApiModelProperty("单据状态")
    private String instructionDocStatusMeaning;

    @ApiModelProperty("成本中心")
    private String costcenterCode;

    @ApiModelProperty("成本中心描述")
    private String description;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("单据行号")
    private String lineNum;

    @ApiModelProperty("行类型")
    @LovValue(value = "HME.INSTRUCTION_TYPE", meaningField = "instructionTypeMeaning")
    private String instructionType;

    @ApiModelProperty("行类型")
    private String instructionTypeMeaning;

    @ApiModelProperty("行状态")
    @LovValue(value = "HME.INSTRUCTION_DOC_STATUS", meaningField = "instructionStatusMeaning")
    private String instructionStatus;

    @ApiModelProperty("行状态")
    private String instructionStatusMeaning;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料版本")
    private String materialVersion;

    @ApiModelProperty("单位")
    private String uomCode;

    @ApiModelProperty("物料组")
    private String itemGroup;

    @ApiModelProperty("物料组描述")
    private String itemGroupDescription;

    @ApiModelProperty("需求数量")
    private String quantity;

    @ApiModelProperty("执行数量")
    private String actualQuantity;

    @ApiModelProperty("发料仓库")
    private String fromWarehouseCode;

    @ApiModelProperty("发料货位")
    private String fromLocatorCode;

    @ApiModelProperty("收料仓库")
    private String toWarehouseCode;

    @ApiModelProperty("收料货位")
    private String toLocatorCode;

    @ApiModelProperty("超发设置")
    @LovValue(value = "WMS.EXCESS_SETTING", meaningField = "excessSettingMeaning")
    private String excessSetting;

    @ApiModelProperty("超发设置")
    private String excessSettingMeaning;

    @ApiModelProperty("超发值")
    private String excessValue;

    @ApiModelProperty("供应商编码")
    private String supplierCode;

    @ApiModelProperty("供应商描述")
    private String supplierName;

    @ApiModelProperty("销单号")
    private String soNum;

    @ApiModelProperty("销单行号")
    private String soLine;

    @ApiModelProperty("订单类型")
    @LovValue(value = "WMS.PO_LINE.TYPE", meaningField = "poTypeMeaning")
    private String poType;

    @ApiModelProperty("订单类型")
    private String poTypeMeaning;

    @ApiModelProperty("制单人")
    private String realName;

    @ApiModelProperty("制单时间")
    private Date creationDate;

    @ApiModelProperty("执行人")
    private String excuteRealName;

    @ApiModelProperty("执行时间")
    private Date lastUpdateDate;

}
