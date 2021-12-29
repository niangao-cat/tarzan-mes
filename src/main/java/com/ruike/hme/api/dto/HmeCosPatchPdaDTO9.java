package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeCosPatchPdaDTO9
 *
 * @author: chaonan.hu@hand-china.com 2020/9/8 11:04:26
 **/
@Data
public class HmeCosPatchPdaDTO9 implements Serializable {
    private static final long serialVersionUID = 7763492003100174821L;

    @ApiModelProperty(value = "工艺路线ID列表", required = true)
    private List<String> operationIdList;

    @ApiModelProperty(value = "容器类型ID", required = true)
    private String containerTypeId;

    @ApiModelProperty(value = "工单Id", required = true)
    private String workOrderId;

    @ApiModelProperty(value = "工位Id", required = true)
    private String workcellId;
}
