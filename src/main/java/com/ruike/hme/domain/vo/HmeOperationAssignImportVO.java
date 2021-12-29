package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 工艺资质关系
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/21 16:19
 */
@Data
public class HmeOperationAssignImportVO {

    @ApiModelProperty("主键")
    private String operationAssignId;

    @ApiModelProperty(value = "工艺主键")
    @NotBlank(message = "工艺主键不能为空")
    private String operationId;

    @ApiModelProperty(value = "资质主键")
    @NotBlank(message = "资质主键不能为空")
    private String qualityId;

    @ApiModelProperty(value = "有效性标识")
    @NotBlank(message = "有效性标识不能为空")
    private String enableFlag;
}
