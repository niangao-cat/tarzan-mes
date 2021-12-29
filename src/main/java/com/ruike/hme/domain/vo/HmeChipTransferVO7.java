package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2020/8/18 13:58
 */
@Data
public class HmeChipTransferVO7 implements Serializable {

    private static final long serialVersionUID = -8534983590793925693L;

    @ApiModelProperty(value = "工位id")
    private String workcellId;

    @ApiModelProperty(value = "工艺")
    private String operationId;

    @ApiModelProperty(value = "芯片类型")
    private String cosType;

    @ApiModelProperty(value = "容器类型")
    private String containerType;
}
