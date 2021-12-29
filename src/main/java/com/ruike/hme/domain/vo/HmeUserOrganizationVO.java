package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;


/**
 * 用户权限导入
 *
 * @author yapeng.yao@hand-china.com 2020/09/10 11:33
 */
@Data
public class HmeUserOrganizationVO implements Serializable {

    private static final long serialVersionUID = -4080468254400746311L;

    @ApiModelProperty(value = "员工ID", required = true)
    private String userId;

    @ApiModelProperty(value = "员工工号", required = true)
    private String userCode;

    @ApiModelProperty(value = "组织类型", required = true)
    private String organizationType;

    @ApiModelProperty(value = "组织ID", required = true)
    private String organizationId;

    @ApiModelProperty(value = "组织编码", required = true)
    private String organizationCode;

    @ApiModelProperty(value = "默认组织标识")
    private String defaultOrganizationFlag;

    @ApiModelProperty(value = "是否有效", required = true)
    private String enableFlag;

    @ApiModelProperty(value = "单据类型")
    private String docType;

    @ApiModelProperty(value = "仓库类型")
    private String locationType;

    @ApiModelProperty(value = "操作类型")
    private String operationType;

    @ApiModelProperty(value = "权限分配是否有效")
    private String privilegeEnableFlag;
}
