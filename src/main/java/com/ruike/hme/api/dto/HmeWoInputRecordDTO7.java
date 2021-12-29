package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 投料记录参数
 *
 * @author jiangling.zheng@hand-china.com 2020-10-27 20:24
 */
@Data
public class HmeWoInputRecordDTO7 implements Serializable {

    private static final long serialVersionUID = -5207327758046328035L;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "工步ID")
    private String routerStepId;
    @ApiModelProperty(value = "组件ID")
    private String bomComponentId;
    @ApiModelProperty(value = "工艺标识")
    private String operationId;
    @ApiModelProperty(value = "单位用量")
    private BigDecimal unitDosage;

}
