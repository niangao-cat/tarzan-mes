package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 实验代码查询
 *
 * @author li.zhang 2021/01/25 13:40
 */
@Data
public class HmeMaterialLotLabCodeVO implements Serializable {

    private static final long serialVersionUID = 5328593698060421215L;

    @ApiModelProperty(value = "主键ID")
    private String labCodeId;

    @ApiModelProperty(value = "步骤ID")
    private String routerStepId;

    @ApiModelProperty(value = "步骤顺序")
    private String sequence;

    @ApiModelProperty(value = "步骤描述")
    private String description;

    @ApiModelProperty(value = "实验代码")
    private String labCode;
}
