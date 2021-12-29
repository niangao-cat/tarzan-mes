package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class HmeEmployeeOutputSummaryVO implements Serializable {
    @ApiModelProperty("数量")
    private BigDecimal qty;
    @ApiModelProperty("进站人/出站人")
    private String siteInBy;
    @ApiModelProperty("物料")
    private String snMaterialId;
    @ApiModelProperty("物料版本")
    private String productionVersion;
    @ApiModelProperty("工序")
    private String processId;
    @ApiModelProperty("返修标识")
    private String reworkFlag;
    @ApiModelProperty("时长")
    private BigDecimal totalProductionTime;
    @ApiModelProperty("不良数量")
    private BigDecimal ngQty;
    @ApiModelProperty("时间")
    private Date jobTime;
    @ApiModelProperty("返修记录标识")
    private String reworkRecordFlag;
    @ApiModelProperty("返修记录数量")
    private BigDecimal reworkRecordQty;
    @ApiModelProperty("EOID")
    private String eoId;
    @ApiModelProperty("进站站时间")
    private Date siteInDate;
}
