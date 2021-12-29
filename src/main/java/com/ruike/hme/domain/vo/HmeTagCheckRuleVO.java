package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 对应数据项导入模板
 *
 * @author Ric 2021/08/27 12:47
 */
@Data
public class HmeTagCheckRuleVO implements Serializable {

    private static final long serialVersionUID = 7655062570057152113L;
    @ApiModelProperty(value = "应用事业部")
    private String businessCode;
    @ApiModelProperty(value = "应用事业部id")
    private String businessId;
    @ApiModelProperty(value = "规则编码")
    private String ruleCode;
    @ApiModelProperty(value = "规则描述")
    private String ruleDescription;
    @ApiModelProperty(value = "分类")
    private String type;
    @ApiModelProperty(value = "物料组编码")
    private String itemGroupCode;
    @ApiModelProperty(value = "物料组编码id")
    private String itemGroupId;
    @ApiModelProperty(value = "当前工序")
    private String workcellName;
    @ApiModelProperty(value = "当前工序id")
    private String WorkcellId;
    @ApiModelProperty(value = "来源工序")
    private String sourceWorkcellName;
    @ApiModelProperty(value = "数据项")
    private String tagCode;
    private String TagId;
    @ApiModelProperty(value = "来源工序id")
    private String sourceWorkcellId;
}
