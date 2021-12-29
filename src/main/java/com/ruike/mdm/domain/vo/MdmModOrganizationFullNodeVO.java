package com.ruike.mdm.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 完整节点组织关系树查询条件
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/6 16:13
 */
@Data
public class MdmModOrganizationFullNodeVO {
    @ApiModelProperty("组织类型")
    private String organizationType;
    @ApiModelProperty("组织ID")
    private String organizationId;
}
