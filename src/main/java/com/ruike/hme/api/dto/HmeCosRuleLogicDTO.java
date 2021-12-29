package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @ClassName HmeCosRuleLogicDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/17 14:14
 * @Version 1.0
 **/
@Data
public class HmeCosRuleLogicDTO implements Serializable {
    private static final long serialVersionUID = -7892148609488082691L;
    @ApiModelProperty("主键ID，标识唯一一条记录")
    private String cosRuleLogicId;
    @ApiModelProperty(value = "头表id")
    private String cosRuleId;
    @ApiModelProperty(value = "组织id")
    private String siteId;
    @ApiModelProperty(value = "规则")
    private String ruleNumber;
    @ApiModelProperty(value = "电流")
    private String current;

    @ApiModelProperty(value = "采集项")
    @LovValue(lovCode = "HME.COS_FUNCTION", meaningField = "collectionItemMeaning")
    private String collectionItem;
    @ApiModelProperty(value = "采集项")
    private String collectionItemMeaning;

    @ApiModelProperty(value = "计算类型")
    @LovValue(lovCode = "HME_COS_COUNT_TYPE", meaningField = "countTypeMeaning")
    private String countType;
    @ApiModelProperty(value = "计算类型")
    private String countTypeMeaning;

    @ApiModelProperty(value = "范围类型")
    @LovValue(lovCode = "HME_RANGE_TYPE", meaningField = "rangeTypeMeaning")
    private String rangeType;
    @ApiModelProperty(value = "范围类型")
    private String rangeTypeMeaning;

    @ApiModelProperty(value = "值")
    private String ruleValue;
}
