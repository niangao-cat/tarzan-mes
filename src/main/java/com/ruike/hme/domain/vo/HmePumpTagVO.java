package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/8/31 7:28
 */
@Data
public class HmePumpTagVO implements Serializable {

    private static final long serialVersionUID = 1917570961638180949L;

    @ApiModelProperty(value = "结果")
    private BigDecimal result;
    @ApiModelProperty(value = "数据项")
    private String tagCode;
}
