package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/1/19 13:56
 */
@Data
public class HmeCosRetestVO implements Serializable {

    private static final long serialVersionUID = 5443174750723465568L;

    @ApiModelProperty("条码")
    private String materialLotCode;
    @ApiModelProperty("工单id")
    private String workOrderId;
    @ApiModelProperty("物料")
    private String materialId;
    @ApiModelProperty("工位")
    private String workcellId;
}
