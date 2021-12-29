package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/6 15:09
 */
@Data
public class HmeTagCheckVO2 implements Serializable {

    private static final long serialVersionUID = -2147411304074265295L;

    @ApiModelProperty("来源工序")
    private String sourceProcessId;
    @ApiModelProperty("来源工序描述")
    private String sourceProcessName;
    @ApiModelProperty("来源工序编码")
    private String sourceProcessCode;
    @ApiModelProperty("数据项ID")
    private String tagId;
    @ApiModelProperty("数据项编码")
    private String tagCode;
    @ApiModelProperty("数据项描述")
    private String tagDescription;
    @ApiModelProperty(value = "下限值")
    private BigDecimal minimumValue;
    @ApiModelProperty(value = "上限值")
    private BigDecimal maximalValue;
    @ApiModelProperty(value = "采集结果")
    private String result;
    @ApiModelProperty(value = "单位")
    private String uomName;
}
