package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * QmsIqcAuditQueryVO2
 * @description: IQC检验审核导出VO
 * @author: chaonan.hu@hand-china.com 2021/03/29 09:38:23
 **/
@Data
@ExcelSheet(zh = "IQC检验审核")
public class QmsIqcAuditQueryVO2 implements Serializable {
    private static final long serialVersionUID = -1202174194406844513L;

    @ApiModelProperty(value = "来源单号")
    @ExcelColumn(zh = "来源单号",order = 0)
    private String instructionDocNum;

    @ApiModelProperty(value = "检验单")
    @ExcelColumn(zh = "检验单",order = 1)
    private String iqcNumber;

    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(zh = "物料编码",order = 2)
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    @ExcelColumn(zh = "物料名称",order = 3)
    private String materialName;

    @ApiModelProperty(value = "物料版本")
    @ExcelColumn(zh = "物料版本",order = 4)
    private String materialVersion;

    @ApiModelProperty(value = "来料数量")
    @ExcelColumn(zh = "来料数量",order = 5)
    private BigDecimal quantity;

    @ApiModelProperty(value = "库位编码")
    @ExcelColumn(zh = "库位",order = 6)
    private String locatorCode;

    @ApiModelProperty(value = "供应商名称")
    @ExcelColumn(zh = "供应商名称",order = 7)
    private String supplierName;

    @ApiModelProperty(value = "接收批次")
    @ExcelColumn(zh = "接收批次",order = 8)
    private String receiptLot;

    @ApiModelProperty(value = "检验类型")
    private String inspectionType;

    @ApiModelProperty(value = "检验类型含义")
    @ExcelColumn(zh = "检验类型",order = 9)
    private String inspectionTypeMeaning;

    @ApiModelProperty(value = "判定不合格时间")
    @ExcelColumn(zh = "判定不合格时间",order = 10)
    private String inspectionFinishDate;

    @ApiModelProperty(value = "到货时间", required = true)
    @ExcelColumn(zh = "到货时间",order = 11)
    private String createdDate;

    @ApiModelProperty(value = "接收人ID")
    private String receiptBy;

    @ApiModelProperty(value = "接收人")
    @ExcelColumn(zh = "接收人",order = 12)
    private String receiptRealName;

    @ApiModelProperty(value = "处理状态")
    private String inspectionStatus;

    @ApiModelProperty(value = "处理状态含义")
    @ExcelColumn(zh = "处理状态",order = 13)
    private String inspectionStatusMeaning;

    @ApiModelProperty(value = "审核结果")
    private String finalDecision;

    @ApiModelProperty(value = "审核结果含义")
    @ExcelColumn(zh = "审核结果",order = 14)
    private String finalDecisionMeaning;

    @ApiModelProperty(value = "检验单标识（如加急）")
    private String identification;

    @ApiModelProperty(value = "是否加急含义")
    @ExcelColumn(zh = "是否加急",order = 15)
    private String identificationMeaning;

    @ApiModelProperty(value = "备注")
    @ExcelColumn(zh = "备注",order = 16)
    private String remark;

    @ApiModelProperty(value = "审核意见")
    @ExcelColumn(zh = "审核意见",order = 17)
    private String auditOpinion;
}
