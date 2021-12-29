package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 不良代码与工艺关系
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/21 19:35
 */
@Data
public class HmeNcValidOperImportVO implements Serializable {

    private static final long serialVersionUID = -9219888566557235301L;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty("主键")
    private String ncValidOperId;

    @ApiModelProperty(value = "不良代码或不良代码组")
    @NotBlank(message = "不良代码或不良代码组不能为空")
    private String 	ncObjectId;

    @ApiModelProperty(value = "类型")
    @NotBlank(message = "类型不能为空")
    private String 	ncObjectType;

    @ApiModelProperty(value = "工艺ID")
    @NotBlank(message = "工艺ID")
    private String operationId;

    @ApiModelProperty(value = "有效性")
    @NotBlank(message = "有效性不能为空")
    private String enableFlag;

    @ApiModelProperty(value = "处置组")
    @NotBlank(message = "处置组")
    private String 	dispositionGroupId;
}
