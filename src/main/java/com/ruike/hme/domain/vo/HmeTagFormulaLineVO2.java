package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/9 20:30
 */
@Data
public class HmeTagFormulaLineVO2 implements Serializable {

    private static final long serialVersionUID = -4373834370874380597L;

    @ApiModelProperty("参数代码")
    private String parameterCode;

    @ApiModelProperty("头数据项Id")
    private String headerTagId;

    @ApiModelProperty("头数据项编码")
    private String headerTagCode;

    @ApiModelProperty("数据项Id")
    private String tagId;

    @ApiModelProperty("数据项编码")
    private String tagCode;

    @ApiModelProperty("结果")
    private BigDecimal resultQty;
}
