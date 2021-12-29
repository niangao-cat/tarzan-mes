package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName HmeCosRuleTypeDTO1
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/27 10:01
 * @Version 1.0
 **/
@Data
public class HmeCosRuleTypeDTO1 implements Serializable {
    private static final long serialVersionUID = -3754732097763808343L;
    @ApiModelProperty(value = "芯片料号")
    private String materialId;
    @ApiModelProperty(value = "芯片类型")
    private String cosType;
    @ApiModelProperty(value = "功率/W（单点）")
    private String powerSinglePoint;
    @ApiModelProperty(value = "芯片数")
    private Long cosCount;


}
