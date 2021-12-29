package com.ruike.hme.api.dto;

import com.ruike.hme.domain.entity.HmeFreezePrivilegeDetail;
import com.ruike.hme.infra.util.BeanCopierUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 冻结权限明细 删除指令
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/1 11:10
 */
@Data
public class HmeFreezePrivilegeDetailDeleteCommand {
    @ApiModelProperty("主键")
    @NotBlank
    private String detailId;
    @ApiModelProperty(value = "权限表ID，外键")
    private String privilegeId;
    @ApiModelProperty(value = "明细对象类型")
    private String detailObjectType;
    @ApiModelProperty(value = "明细对象id")
    private String detailObjectId;
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    public static HmeFreezePrivilegeDetail toEntity(HmeFreezePrivilegeDetailDeleteCommand command) {
        HmeFreezePrivilegeDetail entity = new HmeFreezePrivilegeDetail();
        BeanCopierUtil.copy(command, entity);
        return entity;
    }
}
