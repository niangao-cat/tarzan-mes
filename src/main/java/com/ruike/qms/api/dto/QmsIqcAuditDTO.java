package com.ruike.qms.api.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: IQC审核让步DTO
 * @author: han.zhang
 * @create: 2020/05/20 09:41
 */
@Getter
@Setter
@ToString
public class QmsIqcAuditDTO implements Serializable {
    @ApiModelProperty(value = "检验头Id")
    private String iqcHeaderId;
    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;
    @ApiModelProperty(value = "质检单状态")
    private String inspectionStatus;
    @ApiModelProperty(value = "审核结果")
    private String finalDecision;
    @ApiModelProperty(value = "版本")
    private Long objectVersionNumber;
}