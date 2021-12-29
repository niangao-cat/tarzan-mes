package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description IQC检验平台  查询返回值
 * @Author tong.li
 * @Date 2020/5/12 10:57
 * @Version 1.0
 */
@Data
public class QmsIqcCheckPlatformMainReturnDTO implements Serializable {
    private static final long serialVersionUID = 5842979503620871215L;

    @ApiModelProperty(value = "IQC头标识")
    private String iqcHeaderId;
    @ApiModelProperty(value = "来源单号")
    private String instructionDocNum;
    @ApiModelProperty(value = "检验来源")
    private String docType;
    @ApiModelProperty(value = "来源单ID")
    private String docHeaderId;
    @ApiModelProperty(value = "来源单行ID")
    private String docLineId;
    @ApiModelProperty(value = "检验单")
    private String iqcNumber;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "物料等级")
    private String materialVersion;
    @ApiModelProperty(value = "来料数量")
    private BigDecimal quantity;
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "接收批次")
    private String receiptLot;
    @ApiModelProperty(value = "检验类型")
    @LovValue(value = "QMS.DOC_INSPECTION_TYPE", meaningField = "inspectionTypeMeaning")
    private String inspectionType;
    @ApiModelProperty(value = "检验类型含义")
    private String inspectionTypeMeaning;
    @ApiModelProperty(value = "检验完成时间")
    private Date inspectionFinishDate;
    @ApiModelProperty(value = "到货时间")
    private Date createdDate;
    @ApiModelProperty(value = "接收人")
    private String receiptBy;
    @ApiModelProperty(value = "处理状态")
    @LovValue(value = "QMS.INSPECTION_DOC_STATUS", meaningField = "inspectionStatusMeaning")
    private String inspectionStatus;
    @ApiModelProperty(value = "处理状态含义")
    private String inspectionStatusMeaning;
    @ApiModelProperty(value = "是否加急")
    @LovValue(value = "QMS.IDENTIFICATION", meaningField = "identificationMeaning")
    private String identification;
    @ApiModelProperty(value = "是否加急含义")
    private String identificationMeaning;

    @ApiModelProperty(value = "检验结果")
    private String inspectionResult;

    @ApiModelProperty(value = "检验结果含义")
    private String inspectionResultMeaning;

    @ApiModelProperty(value = "物料版本")
    private String iqcVersion;

    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;

    @ApiModelProperty(value = "样品标识")
    private String sampleFlag;

    @ApiModelProperty(value = "检验组描述")
    private String tagGroupDescription;

    @ApiModelProperty(value = "检验组编码")
    private String tagGroupCode;

    @ApiModelProperty(value = "指令行id")
    private String instructionId;

    @ApiModelProperty(value = "等级汇总")
    private String grade;

    @ApiModelProperty(value = "不良描述")
    private String remark;

    @ApiModelProperty(value = "检验员")
    private String realName;

    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;

    @ApiModelProperty(value = "库位名称")
    private String locatorName;

    @ApiModelProperty(value = "库位编码")
    private String locatorCode;

    @ApiModelProperty(value = "判定不合格时间")
    private String inspectionUnqualifiedDate;
}
