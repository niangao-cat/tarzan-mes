package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

/**
 * <p>
 * 条码冻结权限 查询结果
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/26 17:46
 */
@Data
public class HmeFreezePrivilegeVO {
    @ApiModelProperty("序号")
    private Integer sequenceNum;
    @ApiModelProperty("主键")
    private String privilegeId;
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "账号名")
    private String loginName;
    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "权限类型")
    @LovValue(lovCode = "HME_FREEZE_POWER", meaningField = "privilegeTypeMeaning")
    private String privilegeType;
    @ApiModelProperty(value = "权限类型含义")
    private String privilegeTypeMeaning;
    @ApiModelProperty(value = "COS权限类型")
    @LovValue(lovCode = "HME_COS_FREEZE_POWER", meaningField = "cosPrivilegeTypeMeaning")
    private String cosPrivilegeType;
    @ApiModelProperty(value = "COS权限类型含义")
    private String cosPrivilegeTypeMeaning;
    @ApiModelProperty(value = "有效性")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;
    @ApiModelProperty(value = "有效性")
    private String enableFlagMeaning;
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
