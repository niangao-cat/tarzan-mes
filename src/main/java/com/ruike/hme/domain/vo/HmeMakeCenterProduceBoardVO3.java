package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/30 22:53
 */
@Data
public class HmeMakeCenterProduceBoardVO3 implements Serializable {

    private static final long serialVersionUID = 5705621067693517287L;

    @ApiModelProperty("工单")
    private String workOrderId;
    @ApiModelProperty("工段Id")
    private String lineWorkcellId;
    @ApiModelProperty("产线")
    private String prodLineId;
    @ApiModelProperty("工序")
    private String processId;
    @ApiModelProperty("工段")
    private String lineWorkcellName;
}
