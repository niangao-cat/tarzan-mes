package com.ruike.wms.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import org.hzero.boot.platform.lov.annotation.LovValue;

/**
 * WmsMaterialPostingVO2
 *
 * @author liyuan.lv@hand-china.com 2020/06/13 11:57
 */
@Data
public class WmsMaterialPostingVO2 implements Serializable {

    private static final long serialVersionUID = 96058076203269675L;

    @ApiModelProperty("单据ID")
    private String instructionDocId;
    @ApiModelProperty("送货单")
    private String instructionDocNum;
    @ApiModelProperty("送货单类型")
    private String instructionDocType;
    @ApiModelProperty("送货单状态")
    private String instructionDocStatus;
    @ApiModelProperty("单据行ID")
    private String instructionId;
    @ApiModelProperty("工厂ID")
    private String siteId;
    @ApiModelProperty("指令类型")
    private String instructionType;
    @ApiModelProperty("指令状态")
    private String instructionStatus;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料描述")
    private String materialName;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("制单数量")
    private BigDecimal quantity;
    @ApiModelProperty("单位ID")
    private String uomId;
    @ApiModelProperty("单位")
    private String uomCode;
    @ApiModelProperty("接收仓库ID")
    private String locatorId;
    @ApiModelProperty("供应商ID")
    private String supplierId;
    @ApiModelProperty("接收完成时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date actualReceivedDate;
    @ApiModelProperty("是否免检")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "exemptionFlagMeaning")
    private String exemptionFlag;
    @ApiModelProperty("检验单ID")
    private String iqcHeaderId;
    @ApiModelProperty("检验单")
    private String iqcNumber;
    @ApiModelProperty("检验单类型")
    @LovValue(value = "QMS.DOC_INSPECTION_TYPE", meaningField = "inspectionTypeMeaning")
    private String inspectionType;
    @ApiModelProperty("检验状态")
    @LovValue(value = "QMS.INSPECTION_DOC_STATUS", meaningField = "inspectionStatusMeaning")
    private String inspectionStatus;
    @ApiModelProperty("检验结果")
    @LovValue(value = "QMS.INSPECTION_RESULT", meaningField = "inspectionResultMeaning")
    private String inspectionResult;
    @ApiModelProperty("审核结果")
    @LovValue(value = "QMS.FINAL_DECISION", meaningField = "finalDecisionMeaning")
    private String finalDecision;

    @ApiModelProperty("检验完成时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date inspectionFinishDate;

    @ApiModelProperty("行号")
    @Transient
    private String instructionLineNumber;

    @ApiModelProperty("行状态")
    @Transient
    private String lineStatus;

    @ApiModelProperty("接收数量")
    @Transient
    private String actualReceiveQty;

    @ApiModelProperty("料废调换数量")
    @Transient
    private BigDecimal exchangeQty;

    @ApiModelProperty("已料废调换数量")
    @Transient
    private BigDecimal exchangedQty;

    @ApiModelProperty("接收仓库")
    @Transient
    private String locatorName;

    @ApiModelProperty("指令状态Meaning")
    @Transient
    private String instructionStatusMeaning;

    @ApiModelProperty("是否免检Meaning")
    @Transient
    private String exemptionFlagMeaning;

    @ApiModelProperty("送货单类型Meaning")
    @Transient
    private String inspectionTypeMeaning;

    @ApiModelProperty("检验状态Meaning")
    @Transient
    private String inspectionStatusMeaning;

    @ApiModelProperty("检验结果Meaning")
    @Transient
    private String inspectionResultMeaning;

    @ApiModelProperty("审核结果Meaning")
    @Transient
    private String finalDecisionMeaning;

    @ApiModelProperty("库存调拨指令Id")
    @Transient
    private String transOverInstructionId;

    @ApiModelProperty("库存调拨检验状态")
    @Transient
    private String transOverInspectionStatus;

}
