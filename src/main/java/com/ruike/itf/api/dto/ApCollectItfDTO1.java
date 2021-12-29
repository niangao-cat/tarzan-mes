package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName ApCollectItfDTO1
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/29 19:52
 * @Version 1.0
 **/
@Data
public class ApCollectItfDTO1 implements Serializable {
    private static final long serialVersionUID = -1511990449825144251L;

    @ApiModelProperty(value = "主键")
    private String jobRecordId;
    @ApiModelProperty(value = "限制条件1")
    private String limitCond1;
    @ApiModelProperty(value = "限制条件1的值")
    private String cond1Value;
    @ApiModelProperty(value = "限制条件1")
    private String limitCond2;
    @ApiModelProperty(value = "限制条件2的值")
    private String cond2Value;
    @ApiModelProperty(value = "电流")
    private String valueField;

    @ApiModelProperty(value = "eoId")
    private String eoId;
    @ApiModelProperty(value = "数据组Id")
    private String tagGroupId;
    @ApiModelProperty(value = "数据项Id")
    private String tagId;
    @ApiModelProperty(value = "最小值")
    private BigDecimal minimumValue;
    @ApiModelProperty(value = "最大值")
    private BigDecimal maximalValue;

}
