package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeCosPatchPdaVO5
 *
 * @author: chaonan.hu@hand-china.com 2020/9/2 12:26:34
 **/
@Data
public class HmeCosPatchPdaVO5 implements Serializable {
    private static final long serialVersionUID = 6237101351347043535L;

    private long number;

    private String materialLotId;

    private String materialLotCode;

    private BigDecimal primaryUomQty;

    private String wafer;

    private String jobId;

    private String status;

    private String statusFlag;

    private Date siteOutDate;

    @ApiModelProperty("实验代码")
    private String labCode;

    @ApiModelProperty("实验代码备注")
    private String labRemark;
}
