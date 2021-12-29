package com.ruike.hme.api.dto;

import com.ruike.hme.domain.entity.HmeFreezePrivilegeDetail;
import com.ruike.wms.infra.util.StringCommonUtils;
import io.choerodon.core.exception.CommonException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

import static com.ruike.hme.infra.constant.HmeConstants.FreezePrivilegeObjectType.PROD_LINE;
import static com.ruike.hme.infra.constant.HmeConstants.FreezePrivilegeObjectType.WAREHOUSE;

/**
 * <p>
 * 条码冻结权限明细 查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/1 09:54
 */
@Data
public class HmeFreezePrivilegeDetailQueryDTO implements Serializable {
    private static final long serialVersionUID = -7024682505072924905L;

    @ApiModelProperty(value = "权限表ID")
    @NotBlank
    private String privilegeId;
    @ApiModelProperty(value = "明细类型")
    @NotBlank
    private String detailObjectType;

    public static HmeFreezePrivilegeDetail toEntity(HmeFreezePrivilegeDetailQueryDTO dto) {
        HmeFreezePrivilegeDetail entity = new HmeFreezePrivilegeDetail();
        entity.setPrivilegeId(dto.getPrivilegeId());
        entity.setDetailObjectType(dto.getDetailObjectType());
        return entity;
    }

    public void validation() {
        if (StringUtils.isBlank(this.getPrivilegeId())) {
            throw new CommonException("权限表ID未输入");
        }

        if (!StringCommonUtils.contains(this.getDetailObjectType(), WAREHOUSE, PROD_LINE)) {
            throw new CommonException("明细类型错误");
        }
    }
}
