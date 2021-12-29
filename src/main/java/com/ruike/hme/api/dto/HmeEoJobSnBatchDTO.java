package com.ruike.hme.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: penglin.sui
 * @Date: 2020/11/17 14:56
 * @Description: 投料信息查询参数
 */

@Data
public class HmeEoJobSnBatchDTO implements Serializable {
    private static final long serialVersionUID = -7765676440530410123L;

    @ApiModelProperty(value = "EO ID")
    private String eoId;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "工位ID")
    private String workcellId;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "勾选SN的数量")
    private Integer selectedCount;
    @ApiModelProperty(value = "步骤ID")
    private String routerStepId;
    @ApiModelProperty(value = "jobID")
    private String jobId;
    @ApiModelProperty(value = "工艺路线ID")
    private String routerId;
}
