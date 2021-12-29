package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * QmsPqcHeaderDTO4
 *
 * @author: chaonan.hu@hand-china.com 2020/8/18 09:31:35
 **/
@Data
public class QmsPqcHeaderDTO4 implements Serializable {
    private static final long serialVersionUID = 8540205731104568455L;

    @ApiModelProperty(value = "产线Id", required = true)
    private String prodLineId;

    @ApiModelProperty(value = "类型", required = true)
    private String type;

    @ApiModelProperty(value = "工序Id")
    private String processId;

    @ApiModelProperty(value = "请求标识，1代表pda请求，其他情况代表pc请求")
    private String requestFlag;
}
