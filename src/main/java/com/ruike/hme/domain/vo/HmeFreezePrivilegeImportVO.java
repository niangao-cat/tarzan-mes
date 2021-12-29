package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 冻结解冻权限导入
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/24 13:58
 */
@Data
public class HmeFreezePrivilegeImportVO implements Serializable {

    private static final long serialVersionUID = 3092057795602301196L;

    @ApiModelProperty(value = "租户Id")
    private Long tenantId;

    @ApiModelProperty(value = "用户")
    private String realName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "权限类型")
    private String privilegeType;

    @ApiModelProperty(value = "COS权限类型")
    private String cosPrivilegeType;

    @ApiModelProperty(value = "有效性")
    private String enableFlag;

    @ApiModelProperty(value = "对象类型")
    private String detailObjectType;

    @ApiModelProperty(value = "对象编码")
    private String detailObjectCode;
}
