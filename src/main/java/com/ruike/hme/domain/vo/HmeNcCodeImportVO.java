package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 不良代码导入VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/21 19:17
 */
@Data
public class HmeNcCodeImportVO implements Serializable {

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty("主键")
    private String ncCodeId;

    @ApiModelProperty(value = "站点")
    @NotBlank(message = "站点不能为空")
    private String siteId;

    @ApiModelProperty(value = "不良代码编码")
    @NotBlank(message = "不良代码编码不能为空")
    private String ncCode;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "不良类型")
    @NotBlank(message = "不良类型不能为空")
    private String ncType;

    @ApiModelProperty(value = "是否需要记录组件")
    private String componentRequired;

    @ApiModelProperty(value = "多语言")
    private String ncCodeLang;
}
