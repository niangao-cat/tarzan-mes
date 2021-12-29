package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeCosPatchPdaDTO2
 *
 * @author: chaonan.hu@hand-china.com 2020/8/25 14:53:12
 **/
@Data
public class HmeCosPatchPdaDTO2 implements Serializable {
    private static final long serialVersionUID = -1291053164174289877L;

    @ApiModelProperty(value = "来源条码作业记录Id", required = true)
    private String jobId;

    @ApiModelProperty(value = "工单工艺在制记录Id", required = true)
    private String woJobSnId;

    @ApiModelProperty(value = "来料信息记录Id", required = true)
    private String operationRecordId;

    @ApiModelProperty(value = "条码Id", required = true)
    private String materialLotId;
}
