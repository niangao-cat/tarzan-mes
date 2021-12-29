package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * description
 *
 * @author wengang.qiang@hand-china.com 2021/10/19 13:52
 */
@Data
public class ItfCosMonitorIfaceVO implements Serializable {

    private static final long serialVersionUID = -4529648109863857056L;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "监控单据号")
    private String monitorDocNum;

    @ApiModelProperty(value = "放行类型")
    private String releaseType;

    @ApiModelProperty(value = "放行批次")
    private Long releaseLot;

    @ApiModelProperty(value = "单据/盒子状态")
    private String docStatus;

    @ApiModelProperty(value = "审核状态")
    private String checkStatus;

    @ApiModelProperty("主键ID")
    private String cosMonitorIfaceId;

    @ApiModelProperty(value = "芯片物料编码")
    private String materialCode;

    @ApiModelProperty(value = "cos_type")
    private String cosType;

    @ApiModelProperty(value = "芯片物料描述")
    private String materialName;

    @ApiModelProperty(value = "wafer")
    private String wafer;

    @ApiModelProperty(value = "测试数量")
    private Long testQty;

    @ApiModelProperty(value = "cos良率")
    private BigDecimal passPassRate;

    @ApiModelProperty(value = "放行时间")
    private Date passDate;

    @ApiModelProperty(value = "放行人")
    private String passBy;

    @ApiModelProperty(value = "盒子号")
    private String materialLotCode;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;
}
