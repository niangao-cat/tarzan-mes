package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/8/20 14:43
 */
@Data
public class HmePumpFilterRuleHeaderVO implements Serializable {

    private static final long serialVersionUID = 608773981347039839L;
    @ApiModelProperty(value = "规则编码头ID")
    private String  ruleHeadId;
    @ApiModelProperty(value = "规则编码")
    private String ruleCode;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "泵浦源个数")
    private BigDecimal qty;
    @ApiModelProperty(value = "有效性")
    @LovValue(lovCode = "Z.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;
    @ApiModelProperty(value = "有效性")
    private String enableFlagMeaning;
}
