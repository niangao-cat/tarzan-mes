package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/6/10 16:40
 */
@Data
public class HmeMonthlyPlanVO2 implements Serializable {

    private static final long serialVersionUID = -5443072914149847213L;

    @ApiModelProperty("数量")
    private BigDecimal quantity;
    @ApiModelProperty("物料")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料描述")
    private String materialName;
    @ApiModelProperty("物料类型")
    private String materialType;
    @ApiModelProperty("部门")
    private String businessId;
    @ApiModelProperty("部门名称")
    private String businessName;
    @ApiModelProperty("部门编码")
    private String businessCode;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "月份")
    private String month;
}
