package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 在制报表-工序信息VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/8/3 14:32
 */
@Data
public class HmeProcessInfoVO implements Serializable {

    @ApiModelProperty(value = "物料id")
    private String materialId;

    @ApiModelProperty(value = "工序id")
    private String workcellId;

    @ApiModelProperty(value = "工序名称")
    private String description;

    @ApiModelProperty(value = "运行")
    private BigDecimal runNum;

    @ApiModelProperty(value = "库存")
    private BigDecimal finishNum;
}
