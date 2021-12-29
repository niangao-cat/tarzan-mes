package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * TagCheckRuleHeader表查询条件
 *
 * @author wengang.qiang@hand-china.com 2021/08/25 16:32
 */
@Data
public class TagCheckRuleHeaderDTO implements Serializable {

    private static final long serialVersionUID = -4254034531101613232L;
    @ApiModelProperty(value = "应用事业部")
    private String businessId;
    @ApiModelProperty(value = "物料组编码")
    private String itemGroupId;
    @ApiModelProperty(value = "是否有效")
    private String enableFlag;
    @ApiModelProperty(value = "当前工序")
    private String workcellId;
    @ApiModelProperty(value = "规则编码")
    private String ruleCode;


}
