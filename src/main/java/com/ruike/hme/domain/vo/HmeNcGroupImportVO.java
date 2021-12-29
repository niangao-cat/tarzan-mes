package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 不良代码组
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/21 19:42
 */
@Data
public class HmeNcGroupImportVO implements Serializable {

    private static final long serialVersionUID = 8804083813694817076L;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty("主键")
    private String ncGroupId;

    @ApiModelProperty(value = "站点")
    @NotBlank(message = "站点不能为空")
    private String siteId;

    @ApiModelProperty(value = "不良代码组编码")
    @NotBlank(message = "不良代码组编码")
    private String ncGroupCode;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "优先级")
    @NotNull(message = "优先级不能为空")
    private Long priority;

    @ApiModelProperty(value = "最大限制值")
    @NotNull(message = "最大限制值")
    private Long maxNcLimit;

    @ApiModelProperty(value = "是否需要记录组件")
    @NotBlank(message = "是否需要记录组件")
    private String componentRequired;

    @ApiModelProperty(value = "多语言")
    private String ncGroupLang;
}
