package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * description 接口请求参数
 *
 * @author wengang.qiang@hand-chian.com 2021/09/29 16:46
 */
@Data
public class HmeCosTestMonitorRequestDTO implements Serializable {

    private static final long serialVersionUID = 5798978254531099788L;

    private String cosMonitorIfaceId;

    private Long tenantId;

    @ApiModelProperty(value = "监控单据号")
    private String monitorDocNum;

    private String releaseType;

    private String releaseLot;

    @ApiModelProperty(value = "单据状态")
    private String docStatus;

    @ApiModelProperty(value = "审核状态")
    private String checkStatus;

    @ApiModelProperty(value = "cos类型")
    private String cosType;

    @ApiModelProperty(value = "wafer")
    private String wafer;

    @ApiModelProperty(value = "测试数量")
    private Integer testNum;

    @ApiModelProperty(value = "通过率")
    private BigDecimal passPassRate;

    @ApiModelProperty(value = "放行时间")
    private Date passDate;

    @ApiModelProperty(value = "放行人")
    private Long passBy;

    @ApiModelProperty(value = "芯片物料编码")
    private String materialCode;

    @ApiModelProperty(value = "芯片物料描述")
    private String materialName;

    @ApiModelProperty(value = "盒子号")
    private String materialLotCode;

}
