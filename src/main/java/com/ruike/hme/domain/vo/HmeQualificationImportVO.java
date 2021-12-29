package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 人员资质导入VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/21 15:03
 */
@Data
public class HmeQualificationImportVO implements Serializable {

    @ApiModelProperty("主键")
    private String qualityId;

    @ApiModelProperty(value = "资质类型")
    @NotBlank(message = "资质类型不能为空")
    private String qualityType;

    @ApiModelProperty(value = "资质编码")
    @NotBlank(message = "资质编码不能为空")
    private String 	qualityCode;

    @ApiModelProperty(value = "资质名称")
    @NotBlank(message = "资质名称不能为空")
    private String qualityName;

    @ApiModelProperty(value = "有效性")
    @NotBlank(message = "有效性不能为空")
    private String enableFlag;
}
