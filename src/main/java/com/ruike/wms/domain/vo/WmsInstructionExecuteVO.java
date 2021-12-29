package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/09/09 17:55
 */
@Data
@ExcelSheet(title = "单据执行统计报表")
public class WmsInstructionExecuteVO implements Serializable {

    private static final long serialVersionUID = -4558771865807429979L;

    @ApiModelProperty("工厂Id")
    private String siteId;
    @ApiModelProperty("工厂编码")
    @ExcelColumn(title = "工厂编码", order = 0)
    private String siteCode;
    @ApiModelProperty("单据ID")
    private String instructionDocId;
    @ApiModelProperty("单据号")
    @ExcelColumn(title = "单据号", order = 1)
    private String instructionDocNum;
    @ApiModelProperty("单据类型")
    @LovValue(value = "WX.WMS.INSTRUCTION_DOC_TYPE", meaningField = "instructionDocTypeMeaning")
    private String instructionDocType;
    @ApiModelProperty("单据类型意义")
    @ExcelColumn(title = "单据类型", order = 2)
    private String instructionDocTypeMeaning;
    @ApiModelProperty("单据状态")
    @LovValue(value = "WX.WMS.INSTRUCTION_DOC_STATUS", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;
    @ApiModelProperty("单据状态意义")
    @ExcelColumn(title = "单据状态", order = 3)
    private String instructionDocStatusMeaning;
    @ApiModelProperty("单据行Id")
    private String instructionId;
    @ApiModelProperty("行号")
    @ExcelColumn(title = "行号", order = 4)
    private String instructionLineNum;
    @ApiModelProperty("物料Id")
    private String materialId;
    @ApiModelProperty("物料编码")
    @ExcelColumn(title = "物料编码", order = 5)
    private String materialCode;
    @ApiModelProperty("物料描述")
    @ExcelColumn(title = "物料描述", order = 6)
    private String materialName;
    @ApiModelProperty("单位Id")
    private String uomId;
    @ApiModelProperty("单位编码")
    @ExcelColumn(title = "单位编码", order = 9)
    private String uomCode;
    @ApiModelProperty("制单数量")
    @ExcelColumn(title = "制单数量", order = 10)
    private String quantity;
    @ApiModelProperty("执行数量")
    @ExcelColumn(title = "执行数量", order = 11)
    private Double actualQty;
    @ApiModelProperty("来源仓库Id")
    private String fromLocatorId;
    @ApiModelProperty("来源仓库")
    @ExcelColumn(title = "来源仓库", order = 12)
    private String fromLocatorCode;
    @ApiModelProperty("目标仓库Id")
    private String toLocatorId;
    @ApiModelProperty("目标仓库")
    @ExcelColumn(title = "目标仓库", order = 13)
    private String toLocatorCode;
    @ApiModelProperty("创建人")
    @ExcelColumn(title = "创建人", order = 14)
    private String person;
    @ApiModelProperty("制单时间")
    @ExcelColumn(title = "制单时间", order = 15)
    private String creationDate;
    @ApiModelProperty("制单人")
    @ExcelColumn(title = "制单人", order = 16)
    private String createdBy;
    @ApiModelProperty("最后更新时间")
    @ExcelColumn(title = "最后更新时间", order = 17)
    private String lastUpdateDate;
    @ApiModelProperty("最后更新人")
    @ExcelColumn(title = "最后更新人", order = 18)
    private String lastUpdatedBy;
    @ApiModelProperty("物料组Id")
    private String itemGroupId;
    @ApiModelProperty("物料组编码")
    @ExcelColumn(title = "物料组编码", order = 7)
    private String itemGroupCode;
    @ApiModelProperty("物料组描述")
    @ExcelColumn(title = "物料组描述", order = 8)
    private String itemGroupDescription;
}
