package com.ruike.hme.domain.vo;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 容器
 *
 * @author penglin.sui@hand-china.com 2020/08/18 16:51
 */
@Data
public class HmeContainerImportVO {
    @ApiModelProperty(value = "容器编码")
    @NotBlank(message = "容器编码不能为空")
    private String containerCode;
    @ApiModelProperty(value = "容器类型编码")
    @NotBlank(message = "容器类型编码不能为空")
    private String containerTypeCode;
    @ApiModelProperty(value = "容器状态")
    @NotBlank(message = "容器状态不能为空")
    private String containerStatus;
    @ApiModelProperty(value = "容器名称")
    private String containerName;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "工厂")
    @NotBlank(message = "工厂不能为空")
    private String siteCode;
    @ApiModelProperty(value = "货位")
    private String locatorCode;
}
