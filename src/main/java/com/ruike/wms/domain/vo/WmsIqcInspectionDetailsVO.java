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
 * @author li.zhang 2021/09/09 9:55
 */
@Data
@ExcelSheet(title = "iqc检验明细")
public class WmsIqcInspectionDetailsVO implements Serializable {

    private static final long serialVersionUID = 7851806972536271662L;

    @ApiModelProperty("工厂Id")
    private String siteId;
    @ApiModelProperty("工厂编码")
    @ExcelColumn(title = "工厂编码", order = 0)
    private String siteCode;
    @ApiModelProperty("检验单Id")
    private String iqcHeaderId;
    @ApiModelProperty("检验单号")
    @ExcelColumn(title = "检验单号", order = 1)
    private String iqcNumber;
    @ApiModelProperty("检验类型")
    @LovValue(value = "QMS.INSPECTION_TYPE", meaningField = "inspectionTypeMeaning")
    private String inspectionType;
    @ApiModelProperty("检验类型意义")
    @ExcelColumn(title = "检验类型", order = 2)
    private String inspectionTypeMeaning;
    @ApiModelProperty("检验状态")
    @LovValue(value = "QMS.INSPECTION_DOC_STATUS", meaningField = "inspectionStatusMeaning")
    private String inspectionStatus;
    @ApiModelProperty("检验状态意义")
    @ExcelColumn(title = "检验状态", order = 3)
    private String inspectionStatusMeaning;
    @ApiModelProperty("物料Id")
    private String materialId;
    @ApiModelProperty("物料编码")
    @ExcelColumn(title = "物料编码", order = 4)
    private String materialCode;
    @ApiModelProperty("物料描述")
    @ExcelColumn(title = "物料描述", order = 5)
    private String materialName;
    @ApiModelProperty("供应商Id")
    private String supplierId;
    @ApiModelProperty("供应商编码")
    @ExcelColumn(title = "供应商编码", order = 6)
    private String supplierCode;
    @ApiModelProperty("供应商描述")
    @ExcelColumn(title = "供应商描述", order = 7)
    private String supplierName;
    @ApiModelProperty("检验结果")
    @LovValue(value = "QMS.INSPECTION_RESULT", meaningField = "inspectionResultMeaning")
    private String inspectionResult;
    @ApiModelProperty("检验结果意义")
    @ExcelColumn(title = "检验结果", order = 8)
    private String inspectionResultMeaning;
    @ApiModelProperty("不良项")
    @ExcelColumn(title = "不良项", order = 9)
    private String ngQty;
    @ApiModelProperty("备注")
    @ExcelColumn(title = "备注", order = 10)
    private String remark;
    @ApiModelProperty("接收批次")
    @ExcelColumn(title = "接收批次", order = 11)
    private String receiptLot;
    @ApiModelProperty("检验员")
    @ExcelColumn(title = "检验员", order = 12)
    private String qcBy;
    @ApiModelProperty("审批结果")
    @LovValue(value = "QMS.FINAL_DECISION", meaningField = "finalDecisionMeaning")
    private String finalDecision;
    @ApiModelProperty("审批结果意义")
    @ExcelColumn(title = "审批结果", order = 13)
    private String finalDecisionMeaning;
    @ApiModelProperty("审批意见")
    @ExcelColumn(title = "审批意见", order = 14)
    private String auditOpinion;
    @ApiModelProperty("审核员")
    @ExcelColumn(title = "审核员", order = 15)
    private String lastUpdateBy;
    @ApiModelProperty("行Id")
    private String iqcLineId;
    @ApiModelProperty("检验项目")
    @ExcelColumn(title = "检验项目", order = 16)
    private String inspection;
    @ApiModelProperty("检验项目描述")
    @ExcelColumn(title = "检验项目描述", order = 17)
    private String inspectionDesc;
    @ApiModelProperty("文本规格值")
    @ExcelColumn(title = "文本规格值", order = 18)
    private String standardText;
    @ApiModelProperty("规格范围从")
    @ExcelColumn(title = "规格范围从", order = 19)
    private String standardFrom;
    @ApiModelProperty("规格范围至")
    @ExcelColumn(title = "规格范围至", order = 20)
    private String standardTo;
//    @ApiModelProperty("抽样方案类型")
//    @LovValue(value = "QMS.IQC_SAMPLE_TYPE", meaningField = "sampleType")
//    private String sampleTypeId;
    @ApiModelProperty("抽样方案类型意义")
    @ExcelColumn(title = "抽样方案类型", order = 21)
    private String sampleType;
    @ApiModelProperty("抽样数量")
    @ExcelColumn(title = "抽样数量", order = 22)
    private String sampleSize;
    @ApiModelProperty("AC/RE")
    @ExcelColumn(title = "AC/RE", order = 23)
    private String acRe;
    @ApiModelProperty("结论")
    @LovValue(value = "QMS.INSPECTION_RESULT", meaningField = "lineInspectionResultMeaning")
    private String lineInspectionResult;
    @ApiModelProperty("结论意义")
    @ExcelColumn(title = "结论", order = 24)
    private String lineInspectionResultMeaning;
    @ApiModelProperty("行明细Id")
    private String iqcDetailsId;
    @ApiModelProperty("序列号")
    @ExcelColumn(title = "序列号", order = 25)
    private String number;
    @ApiModelProperty("测试值")
    @ExcelColumn(title = "测试值", order = 26)
    private String result;
    @ApiModelProperty("检验工具")
    @LovValue(value = "QMS.INSPECTION_TOOL", meaningField = "inspectionToolMeaning")
    private String inspectionTool;
    @ApiModelProperty("检验工具意义")
    @ExcelColumn(title = "检验工具", order = 27)
    private String inspectionToolMeaning;
    @ApiModelProperty("检验完成时间")
    @ExcelColumn(title = "检验完成时间", order = 28)
    private String inspectionFinishDate;
    @ApiModelProperty("创建时间")
    @ExcelColumn(title = "创建时间", order = 29)
    private String creationDate;
}
