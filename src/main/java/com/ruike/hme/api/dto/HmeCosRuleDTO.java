package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName HmeCosRuleDTO
 * @Description TODO 查询规则头表数据
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/10 16:39
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
public class HmeCosRuleDTO  implements Serializable {
    private static final long serialVersionUID = 2913161606926080083L;

    @ApiModelProperty(value = "规则编码")
    private String cosRuleCode;

    @ApiModelProperty(value = "芯片序列号")
    private String siteId;

}
