package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class WmsStandingWarehouseOutboundPlatformReturnDTO implements Serializable {
    private static final long serialVersionUID = 131010351369397034L;
    @ApiModelProperty(value = "出库任务--任务号 ")
    private String taskNum;
    @ApiModelProperty(value = "任务发起时间")
    private Date creationDate;
    @ApiModelProperty(value = "响应任务时间")
    private Date lastUpdateDate;
    @ApiModelProperty(value = "等待时长")
    private String lastCreationDate;
    @ApiModelProperty(value = "执行时长")
    private String executeTime;
    @ApiModelProperty(value = "出库任务区域")
    private List<WmsStandingWarehouseOutboundPlatformReturnDTO3> returnDTO3List;

    @ApiModelProperty(value = "出库任务区域")
    private List<WmsStandingWarehouseOutboundPlatformReturnDTO2> returnDTO2List;


}
