package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeVisualInspectionVO
 *
 * @author: chaonan.hu@hand-china.com 2020/01/20 15:32:12
 **/
@Data
public class HmeVisualInspectionVO implements Serializable {
    private static final long serialVersionUID = -3002385849428112090L;

    @ApiModelProperty(value = "jobId")
    private String jobId;

    @ApiModelProperty(value = "物料批Id")
    private String materialLotId;

    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;

    @ApiModelProperty(value = "数量")
    private BigDecimal snQty;

    @ApiModelProperty(value = "工单Id")
    private String workOrderId;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "WAFER")
    private String waferNum;

    @ApiModelProperty(value = "芯片类型")
    private String cosType;

    @ApiModelProperty(value = "工艺步骤加工次数")
    private String eoStepNum;

    @ApiModelProperty(value = "出站时间")
    private Date siteOutDate;

    @ApiModelProperty(value = "进站时间")
    private Date siteInDate;
}
