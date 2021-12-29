package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 偏振度&发散角良率维护 行表查询返回字段
 *
 * @author wengang.qiang@hand-china.com 2021/09/14 15:34
 */
@Data
public class HmeTagPassRateLineVO implements Serializable {

    private static final long serialVersionUID = 9206232140081052704L;

    @ApiModelProperty("主键")
    private String lineId;
    @ApiModelProperty("头id")
    private String headerId;
    @ApiModelProperty(value = "加测目标良率")
    private BigDecimal addPassRate;

    @ApiModelProperty(value = "测试总量")
    private Long testSumQty;

    @ApiModelProperty(value = "优先级")
    private Long priority;

    @ApiModelProperty(value = "备注")
    private String remark;
}
