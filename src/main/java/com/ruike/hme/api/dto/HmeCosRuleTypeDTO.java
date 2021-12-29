package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @ClassName HmeCosRuleTypeDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/17 13:59
 * @Version 1.0
 **/
@Data
public class HmeCosRuleTypeDTO  implements Serializable {

    private static final long serialVersionUID = -6035160527832831173L;

    @ApiModelProperty("主键ID，标识唯一一条记录")
    private String cosRuleTypeId;
    @ApiModelProperty(value = "头表id")
    private String cosRuleId;
    @ApiModelProperty(value = "组织id")
    private String siteId;
    @ApiModelProperty(value = "芯片路数")
    private Long cosNumber;
    @ApiModelProperty(value = "芯片料号ID")
    private String materialId;

    @ApiModelProperty(value = "芯片料号")
    private String materialCode;

    @ApiModelProperty(value = "芯片类型")
    @LovValue(lovCode = "HME_COS_TYPE", meaningField = "cosTypeMeaning")
    private String cosType;
    @ApiModelProperty(value = "芯片类型含义")
    private String cosTypeMeaning;

    @ApiModelProperty(value = "功率/W（单点）")
    @LovValue(lovCode = "HME_COS_POWER", meaningField = "powerSinglePointMeaning")
    private String powerSinglePoint;
    @ApiModelProperty(value = "功率/W（单点）")
    private String powerSinglePointMeaning;
}
