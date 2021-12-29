package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeCosYieldComputeVO
 *
 * @author: chaonan.hu@hand-china.com 2021/09/17 15:23
 **/
@Data
public class HmeCosYieldComputeVO implements Serializable {
    private static final long serialVersionUID = 5037713255472511101L;

    @ApiModelProperty(value = "COS类型或wafer的值")
    private String attrValue;

    private String materialLotId;

    private String materialLotCode;

    private String materialId;

    private String materialCode;

    private String enableFlag;
}
