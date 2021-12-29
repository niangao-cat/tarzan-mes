package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: chaonan.hu@hand-china.com 2020-07-01 10:27:11
 **/
@Data
public class HmeNcDisposePlatformDTO12 extends HmeNcDisposePlatformDTO6 implements Serializable {
    private static final long serialVersionUID = -1926672838768511186L;

    @ApiModelProperty(value = "工艺Id")
    private String operationId;

    @ApiModelProperty(value = "工艺描述")
    private String operationName;

    @ApiModelProperty(value = "工步Id")
    private String eoStepActualId;

    @ApiModelProperty(value = "工步描述")
    private String eoStepActualName;
}
