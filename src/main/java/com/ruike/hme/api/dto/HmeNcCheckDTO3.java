package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 不良审核
 *
 * @author chaonan.hu@hand-china.com 2020-07-20 14:59:18
 */
@Data
public class HmeNcCheckDTO3 implements Serializable {
    private static final long serialVersionUID = 9174278132641069515L;

    @ApiModelProperty(value = "工艺ID")
    private String rootCauseOperationId;

    @ApiModelProperty(value = "不良代码组编码")
    private String ncGroupCode;

    @ApiModelProperty(value = "不良代码组描述")
    private String description;
}
