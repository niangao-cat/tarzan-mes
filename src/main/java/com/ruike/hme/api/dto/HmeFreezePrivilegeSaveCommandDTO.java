package com.ruike.hme.api.dto;

import com.ruike.hme.domain.entity.HmeFreezePrivilege;
import com.ruike.hme.domain.entity.HmeFreezePrivilegeDetail;
import com.ruike.hme.infra.util.BeanCopierUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 条码冻结权限 保存命令
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/1 09:29
 */
@Data
public class HmeFreezePrivilegeSaveCommandDTO implements Serializable {
    private static final long serialVersionUID = 8758032229028410014L;

    @ApiModelProperty("主键")
    private String privilegeId;
    @ApiModelProperty(value = "用户ID")
    @NotNull
    private Long userId;
    @ApiModelProperty(value = "邮箱")
    @NotBlank
    private String email;
    @ApiModelProperty(value = "权限类型")
    @NotBlank
    private String privilegeType;
    @ApiModelProperty(value = "权限类型")
    @NotBlank
    private String cosPrivilegeType;
    @ApiModelProperty(value = "有效性")
    @NotBlank
    private String enableFlag;
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    @ApiModelProperty(value = "行列表")
    private List<LineCommand> lineList;

    public static HmeFreezePrivilege toEntity(HmeFreezePrivilegeSaveCommandDTO command) {
        HmeFreezePrivilege entity = new HmeFreezePrivilege();
        BeanCopierUtil.copy(command, entity);
        return entity;
    }

    public static List<HmeFreezePrivilegeDetail> lineBatchToEntity(List<LineCommand> dtoList) {
        List<HmeFreezePrivilegeDetail> lineList = new ArrayList<>();
        dtoList.forEach(r -> lineList.add(LineCommand.toEntity(r)));
        return lineList;
    }

    @Data
    public static class LineCommand {
        @ApiModelProperty("主键")
        private String detailId;
        @ApiModelProperty(value = "权限表ID，外键")
        private String privilegeId;
        @ApiModelProperty(value = "明细对象类型")
        private String detailObjectType;
        @ApiModelProperty(value = "明细对象id")
        private String detailObjectId;
        @ApiModelProperty(value = "租户ID")
        private Long tenantId;

        public static HmeFreezePrivilegeDetail toEntity(LineCommand command) {
            HmeFreezePrivilegeDetail entity = new HmeFreezePrivilegeDetail();
            BeanCopierUtil.copy(command, entity);
            return entity;
        }
    }
}
