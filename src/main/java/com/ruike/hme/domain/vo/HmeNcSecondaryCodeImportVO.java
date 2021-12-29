package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * description
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/21 19:54
 */
@Data
public class HmeNcSecondaryCodeImportVO implements Serializable {

    private static final long serialVersionUID = -453024713135710041L;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty("主键")
    private String ncSecondaryCodeId;

    @ApiModelProperty(value = "不良代码或不良代码组")
    @NotBlank(message = "不良代码或不良代码组不能为空")
    private String ncObjectId;

    @ApiModelProperty(value = "类型")
    @NotBlank(message = "类型不能为空")
    private String ncObjectType;

    @ApiModelProperty(value = "不良代码")
    @NotBlank(message = "不良代码不能为空")
    private String ncCodeId;

    @ApiModelProperty(value = "顺序")
    @NotNull(message = "顺序不能为空")
    private Long sequence;

    @ApiModelProperty(value = "关闭是否需要")
    @NotBlank(message = "关闭是否需要不能为空")
    private String requiredFlag;
}
