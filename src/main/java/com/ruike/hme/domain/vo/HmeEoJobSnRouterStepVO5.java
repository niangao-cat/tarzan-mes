package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.method.domain.vo.MtRouterStepVO5;

@Data
public class HmeEoJobSnRouterStepVO5 extends MtRouterStepVO5 implements Serializable {
    private static final long serialVersionUID = -6091102012979367859L;

    @ApiModelProperty("EOID")
    private String eoId;
    @ApiModelProperty("工艺ID")
    private String routerId;
}
