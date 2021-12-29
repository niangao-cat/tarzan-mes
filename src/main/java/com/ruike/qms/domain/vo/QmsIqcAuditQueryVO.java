package com.ruike.qms.domain.vo;

import com.ruike.qms.domain.entity.QmsIqcHeader;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: iqc审核查询VO
 * @author: han.zhang
 * @create: 2020/05/19 11:11
 */
@Getter
@Setter
@ToString
public class QmsIqcAuditQueryVO extends QmsIqcHeader implements Serializable {
    private static final long serialVersionUID = -2081783619423899269L;

    @ApiModelProperty(value = "来源单号")
    private String instructionDocNum;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "检验类型含义")
    private String inspectionTypeMeaning;
    @ApiModelProperty(value = "是否加急含义")
    private String identificationMeaning;
    @ApiModelProperty(value = "接收人姓名")
    private String receiptRealName;
    @ApiModelProperty(value = "检验状态含义")
    private String inspectionStatusMeaning;
    @ApiModelProperty(value = "来源单号")
    private String sourceOrderNum;
    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "检验方式含义")
    private String inspectionMethodMeaning;
    @ApiModelProperty(value = "检验结果含义")
    private String inspectionResultMeaning;
    @ApiModelProperty(value = "检验来源含义")
    private String docTypeMeaning;
    @ApiModelProperty(value = "审核结果")
    @LovValue(value = "QMS.FINAL_DECISION", meaningField = "finalDecisionMeaning")
    private String finalDecision;
    @ApiModelProperty(value = "审核结果含义")
    private String finalDecisionMeaning;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;

    @ApiModelProperty(value = "库位名称")
    private String locatorName;

    @ApiModelProperty(value = "库位编码")
    private String locatorCode;
}