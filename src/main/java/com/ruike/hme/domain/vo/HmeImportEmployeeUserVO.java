package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class HmeImportEmployeeUserVO implements Serializable {

    private static final long serialVersionUID = -107505886517527796L;

    @ApiModelProperty(value = "员工编码")
    @NotBlank(message = "员工编码不能为空")
    private String employeeNum;

    @ApiModelProperty(value = "登录名")
    @NotBlank(message = "登录名不能为空")
    private String loginName;

    @ApiModelProperty(value = "员工主键")
    private String employeeId;

    @ApiModelProperty(value = "用户主键")
    private String userId;

    @ApiModelProperty(value = "主键")
    private String employeeUserId;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;

}
