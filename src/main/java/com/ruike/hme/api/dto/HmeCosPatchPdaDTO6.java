package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeCosPatchPdaDTO6
 *
 * @author: chaonan.hu@hand-china.com 2020/8/29 11:29:34
 **/
@Data
public class HmeCosPatchPdaDTO6 implements Serializable {
    private static final long serialVersionUID = 984788726043457509L;

    @ApiModelProperty(value = "工位Id", required = true)
    private String workcellId;

    @ApiModelProperty(value = "扫描条码", required = true)
    private String materialLotCode;

    @ApiModelProperty(value = "工单ID", required = true)
    private String workOrderId;

    @ApiModelProperty(value = "工艺路线ID列表", required = true)
    private List<String> operationIdList;

    @ApiModelProperty(value = "工段ID", required = true)
    private String wkcLineId;
}
