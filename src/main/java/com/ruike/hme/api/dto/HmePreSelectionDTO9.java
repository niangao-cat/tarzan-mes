package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmePreSelectionDTO9
 *
 * @author: chaonan.hu@hand-china.com 2021/6/21 11:26:41
 **/
@Data
public class HmePreSelectionDTO9 implements Serializable {
    private static final long serialVersionUID = 5646968756653464491L;

    @ApiModelProperty(value = "范围类型")
    private String rangeType;

    @ApiModelProperty(value = "值")
    private String ruleValue;
}
