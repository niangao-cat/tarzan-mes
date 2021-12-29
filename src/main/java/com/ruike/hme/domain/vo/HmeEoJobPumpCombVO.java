package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeEoJobPumpCombVO
 *
 * @author: chaonan.hu@hand-china.com 2021/08/23 11:50
 **/
@Data
public class HmeEoJobPumpCombVO implements Serializable {
    private static final long serialVersionUID = -8472779226034317055L;

    @ApiModelProperty(value = "物料序列号")
    private String snNum;
}
