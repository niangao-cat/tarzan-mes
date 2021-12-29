package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeCosDegreeTestActualVO
 *
 * @author: chaonan.hu@hand-china.com 2021/9/14 14:34
 **/
@Data
public class HmeCosDegreeTestActualVO implements Serializable {
    private static final long serialVersionUID = 827938733332471337L;

    @ApiModelProperty(value = "主键")
    private String headerId;

    @ApiModelProperty(value = "cos类型")
    private String cosType;

    @ApiModelProperty(value = "良率")
    private BigDecimal passRate;
}
