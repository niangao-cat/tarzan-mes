package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/22 16:19
 */
@Data
public class HmePumpCombDTO2 implements Serializable {

    private static final long serialVersionUID = 4327817863445555794L;

    @ApiModelProperty(value = "结果")
    private BigDecimal result;
    @ApiModelProperty(value = "数据项")
    private String tagCode;
}
