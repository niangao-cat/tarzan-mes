package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeFreezePrivilegeDetail;
import com.ruike.hme.infra.util.BeanCopierUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 条码冻结权限明细 查询结果
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/26 17:53
 */
@Data
public class HmeFreezePrivilegeDetailVO {
    @ApiModelProperty("序号")
    private Integer sequenceNum;
    @ApiModelProperty("主键")
    private String detailId;
    @ApiModelProperty(value = "权限表ID，外键")
    private String privilegeId;
    @ApiModelProperty(value = "明细对象类型")
    private String detailObjectType;
    @ApiModelProperty(value = "明细对象id")
    private String detailObjectId;
    @ApiModelProperty(value = "明细对象编码")
    private String detailObjectCode;
    @ApiModelProperty(value = "明细对象名称")
    private String detailObjectName;
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    public static HmeFreezePrivilegeDetailVO toRepresentation(HmeFreezePrivilegeDetail entity) {
        HmeFreezePrivilegeDetailVO representation = new HmeFreezePrivilegeDetailVO();
        BeanCopierUtil.copy(entity, representation);
        return representation;
    }

    public void representationDisplayField(HmeFreezePrivilegeDetailObjectVO detailObject) {
        this.setDetailObjectCode(detailObject.getDetailObjectCode());
        this.setDetailObjectName(detailObject.getDetailObjectName());
    }
}
