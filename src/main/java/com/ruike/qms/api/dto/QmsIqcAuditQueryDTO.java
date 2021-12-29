package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: iqc审核查询DTO
 * @author: han.zhang
 * @create: 2020/05/19 11:12
 */
@Getter
@Setter
@ToString
public class QmsIqcAuditQueryDTO implements Serializable {
    @ApiModelProperty(value = "来源单号")
    private String instructionDocNum;
    @ApiModelProperty(value = "检验单")
    private String iqcNumber;
    @ApiModelProperty(value = "处理状态")
    private String inspectionStatus;
    @ApiModelProperty(value = "是否加急")
    private String identification;
    @ApiModelProperty(value = "检验结果")
    private String inspectionResult;
    @ApiModelProperty(value = "检验头id")
    private String iqcHeaderId;
    @ApiModelProperty(value = "物料Id")
    private String materialId;
    @ApiModelProperty(value = "审核结果")
    private String finalDecision;
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
    @ApiModelProperty(value = "接收批次")
    private String receiptLot;
    @ApiModelProperty(value = "建单时间从")
    private String createdDateFrom;
    @ApiModelProperty(value = "建单时间至")
    private String createdDateTo;
    @ApiModelProperty(value = "是否根据用户与物料组关系限制查看单据标识，为REL时限制")
    private String relFlag;
    @ApiModelProperty(value = "库位编码")
    private String locatorCode;
}
