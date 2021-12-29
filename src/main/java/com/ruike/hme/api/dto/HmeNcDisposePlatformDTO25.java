package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: chaonan.hu@hand-china.com 2020-08-15 16:41:12
 **/
@Data
public class HmeNcDisposePlatformDTO25 implements Serializable {
    private static final long serialVersionUID = -8234814596263975373L;

    @ApiModelProperty(value = "序列号", required = true)
    private String materialLotCode;

    @ApiModelProperty(value = "不良代码组Id", required = true)
    private String ncGroupId;

    @ApiModelProperty(value = "本库位Id", required = true)
    private String currentwWorkcellId;

    @ApiModelProperty(value = "责任库位Id", required = true)
    private String workcellId;

    @ApiModelProperty(value = "工步id")
    private String eoStepActualId;

    @ApiModelProperty(value = "工艺id")
    private String rootCauseOperationId;
}
