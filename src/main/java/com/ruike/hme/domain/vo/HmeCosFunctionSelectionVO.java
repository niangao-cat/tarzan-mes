package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeCosFunctionSelectionVO
 *
 * @author: chaonan.hu@hand-china.com 2021/08/19 10:26
 **/
@Data
public class HmeCosFunctionSelectionVO implements Serializable {
    private static final long serialVersionUID = 14074207599210370L;

    @ApiModelProperty("芯片序列号")
    private String loadSequence;

    @ApiModelProperty("电流点")
    private String current;
}
