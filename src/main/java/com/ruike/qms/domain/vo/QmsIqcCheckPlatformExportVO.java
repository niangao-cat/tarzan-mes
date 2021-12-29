package com.ruike.qms.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * QmsIqcCheckPlatformExportVO
 *
 * @author: chaonan.hu@hand-china.com 2020/11/11 10:42
 **/
@Data
@ExcelSheet(zh = "IQC检验单")
public class QmsIqcCheckPlatformExportVO implements Serializable {
    private static final long serialVersionUID = 4080213099852186800L;

    @ApiModelProperty(value = "IQC头标识")
    private String iqcHeaderId;
    @ApiModelProperty(value = "来源单号")
    @ExcelColumn(zh = "来源单号",order = 1)
    private String instructionDocNum;
    @ApiModelProperty(value = "检验来源")
    private String docType;
    @ApiModelProperty(value = "来源单ID")
    private String docHeaderId;
    @ApiModelProperty(value = "来源单行ID")
    private String docLineId;
    @ApiModelProperty(value = "检验单")
    @ExcelColumn(zh = "检验单",order = 2)
    private String iqcNumber;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    @ExcelColumn(zh = "物料编码",order = 6)
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    @ExcelColumn(zh = "物料描述",order = 7)
    private String materialName;
    @ApiModelProperty(value = "物料版本")
    @ExcelColumn(zh = "物料版本",order = 24)
    private String materialVersion;
    @ApiModelProperty(value = "来料数量")
    @ExcelColumn(zh = "来料数量",order = 8)
    private BigDecimal quantity;
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商名称")
    @ExcelColumn(zh = "供应商名称",order = 11)
    private String supplierName;
    @ApiModelProperty(value = "接收批次")
    @ExcelColumn(zh = "接收批次",order = 14)
    private String receiptLot;
    @ApiModelProperty(value = "检验类型")
    @LovValue(value = "QMS.DOC_INSPECTION_TYPE", meaningField = "inspectionTypeMeaning")
    private String inspectionType;
    @ApiModelProperty(value = "检验类型含义")
    @ExcelColumn(zh = "检验类型",order = 3)
    private String inspectionTypeMeaning;
    @ApiModelProperty(value = "检验完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionFinishDate;
    @ApiModelProperty(value = "检验完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelColumn(zh = "检验完成时间",order = 17)
    private String inspectionFinishDateStr;
    @ApiModelProperty(value = "到货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;
    @ApiModelProperty(value = "到货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelColumn(zh = "到货日期",order = 13)
    private String createdDateStr;
    @ApiModelProperty(value = "接收人")
    @ExcelColumn(zh = "接收人",order = 21)
    private String receiptBy;
    @ApiModelProperty(value = "处理状态")
    @LovValue(value = "QMS.INSPECTION_DOC_STATUS", meaningField = "inspectionStatusMeaning")
    private String inspectionStatus;
    @ApiModelProperty(value = "处理状态含义")
    @ExcelColumn(zh = "处理状态",order = 4)
    private String inspectionStatusMeaning;
    @ApiModelProperty(value = "是否加急")
    @LovValue(value = "QMS.IDENTIFICATION", meaningField = "identificationMeaning")
    private String identification;
    @ApiModelProperty(value = "是否加急含义")
    @ExcelColumn(zh = "是否加急",order = 22)
    private String identificationMeaning;

    @ApiModelProperty(value = "检验结果")
    private String inspectionResult;

    @ApiModelProperty(value = "检验结果含义")
    @ExcelColumn(zh = "检验结果",order = 5)
    private String inspectionResultMeaning;

    @ApiModelProperty(value = "物料版本")
    @ExcelColumn(zh = "物料版本",order = 23)
    private String iqcVersion;

    @ApiModelProperty(value = "供应商批次")
    @ExcelColumn(zh = "供应商批次",order = 12)
    private String supplierLot;

    @ApiModelProperty(value = "备注")
    @ExcelColumn(zh = "备注",order = 18)
    private String remark;

    @ApiModelProperty(value = "检验员")
    @ExcelColumn(zh = "检验员",order = 20)
    private String realName;

    @ApiModelProperty(value = "审核意见")
    @ExcelColumn(zh = "审核意见",order = 19)
    private String auditOpinion;

    @ApiModelProperty(value = "库位编码")
    @ExcelColumn(zh = "库位",order = 9)
    private String locatorCode;

    @ApiModelProperty(value = "等级汇总")
    @ExcelColumn(zh = "等级汇总",order = 10)
    private String grade;

    @ApiModelProperty(value = "样品标识")
    @ExcelColumn(zh = "样品标识", order = 15)
    private String sampleFlag;

    @ApiModelProperty(value = "判定不合格时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionUnqualifiedDate;

    @ApiModelProperty(value = "判定不合格时间")
    @ExcelColumn(zh = "判定不合格时间", order = 16)
    private String inspectionUnqualifiedDateStr;

    @ApiModelProperty(value = "检验组编码")
    @ExcelColumn(zh = "检验组编码", order = 25)
    private String tagGroupCode;

    @ApiModelProperty(value = "检验组描述")
    @ExcelColumn(zh = "检验组描述", order = 26)
    private String tagGroupDescription;

    @ApiModelProperty(value = "指令行id")
    private String instructionId;

}
