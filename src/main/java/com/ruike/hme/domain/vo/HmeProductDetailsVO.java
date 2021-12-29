package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/8 18:42
 */
@Data
public class HmeProductDetailsVO implements Serializable {

    private static final long serialVersionUID = 339044781649099765L;

    @ApiModelProperty(value = "物料id")
    private String materialId;

    @ApiModelProperty(value = "工位id")
    private String workcellId;

    @ApiModelProperty(value = "工位描述")
    private String description;

    @ApiModelProperty(value = "运行数量")
    private BigDecimal runNum;

    @ApiModelProperty(value = "库存数量")
    private BigDecimal finishNum;
}
