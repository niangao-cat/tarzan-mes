package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * HmeInspectorItemGroupRelVO
 * @description: 分页查询返回对象VO
 * @author: chaonan.hu@hand-china.com 2021/03/29 14:05:41
 **/
@Data
public class HmeInspectorItemGroupRelVO implements Serializable {
    private static final long serialVersionUID = -1892769864185279774L;

    @ApiModelProperty(value = "主键")
    private String relId;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "用户账号")
    private String loginName;

    @ApiModelProperty(value = "用户姓名")
    private String realName;

    @ApiModelProperty(value = "权限类型")
    @LovValue(value = "QMS.INSPECT_POWER_TYPE", meaningField = "privilegeTypeMeaning")
    private String privilegeType;

    @ApiModelProperty(value = "权限类型含义")
    private String privilegeTypeMeaning;

    @ApiModelProperty(value = "物料组ID")
    private String itemGroupId;

    @ApiModelProperty(value = "物料组编码")
    private String itemGroupCode;

    @ApiModelProperty(value = "物料组描述")
    private String itemGroupDescription;

    @ApiModelProperty(value = "有效性")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;

    @ApiModelProperty(value = "有效性含义")
    private String enableFlagMeaning;
}
