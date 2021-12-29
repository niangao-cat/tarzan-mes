package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 在制报表eo信息
 *
 * @author sanfeng.zhang@hand-china.com 2020/7/29 11:35
 */
@Data
public class HmeProductEoInfoVO implements Serializable {

    private static final long serialVersionUID = 7187709459070848370L;

    @ApiModelProperty(value = "产线id")
    private String productionLineId;

    @ApiModelProperty(value = "eo编码")
    private String eoNum;

    @ApiModelProperty(value = "eo标识")
    private String eoIdentification;

    @ApiModelProperty(value = "返修标识")
    private String validateFlag;

    @ApiModelProperty(value = "数量")
    private BigDecimal qtyNum;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "工序Id")
    private String workcellId;

    @ApiModelProperty(value = "标识 Y-运行 N-库存 Q-排队")
    private String flag;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "操作时间")
    private Date lastUpdateDate;

    @ApiModelProperty(value = "停留时长")
    private Long timeDiff;

    @ApiModelProperty(value = "标准时长")
    private BigDecimal standardTimer;

    @ApiModelProperty(value = "工序Id")
    private String processId;

    @ApiModelProperty(value = "工单号")
    private String workOrderId;

    @ApiModelProperty(value = "结班时间")
    private String shiftEndTime;
    @ApiModelProperty(value = "开班时间")
    private String shiftStartTime;
}
