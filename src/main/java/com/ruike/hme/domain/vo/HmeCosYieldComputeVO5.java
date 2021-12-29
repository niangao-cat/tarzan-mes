package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeCosYieldComputeVO3
 *
 * @author: chaonan.hu@hand-china.com 2021/09/17 16:14
 **/
@Data
public class HmeCosYieldComputeVO5 implements Serializable {
    private static final long serialVersionUID = 4174874136633290818L;

    @ApiModelProperty(value = "租户")
    private Long tenantId;

    @ApiModelProperty(value = "COS测试良率监控记录表主键")
    private String cosMonitorRecordId;

    @ApiModelProperty(value = "COS测试良率监控头表主键")
    private String cosMonitorHeaderId;

    @ApiModelProperty(value = "监控单据号")
    private String monitorDocNum;

    @ApiModelProperty(value = "单据状态")
    private String docStatus;

    @ApiModelProperty(value = "审核状态")
    private String checkStatus;

    @ApiModelProperty(value = "cos类型")
    private String cosType;

    @ApiModelProperty(value = "wafer")
    private String wafer;

    @ApiModelProperty(value = "创建日期")
    private Date creationDate;

    @ApiModelProperty(value = "cos良率")
    private BigDecimal testPassRate;

    @ApiModelProperty(value = "cid")
    private Long cid;

    @ApiModelProperty(value = "版本号")
    private Long objectVersionNumber;

    @ApiModelProperty(value = "创建人")
    private Long createdBy;

    @ApiModelProperty(value = "最后更新人")
    private Long lastUpdatedBy;

    @ApiModelProperty(value = "最后更新日期")
    private Date lastUpdateDate;

    @ApiModelProperty(value = "测试数量")
    private Long testQty;
}
