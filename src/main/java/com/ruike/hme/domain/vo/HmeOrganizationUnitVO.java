package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 组织职能关系VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/7/28 12:38
 */
@Data
public class HmeOrganizationUnitVO implements Serializable {
    private static final long serialVersionUID = 3353067549510034897L;

    @ApiModelProperty(value = "部门")
    private Long unitId;

    @ApiModelProperty(value = "部门名称")
    private String unitName;

    @ApiModelProperty(value = "工作单元id")
    private String organizationId;

    @ApiModelProperty(value = "工作单元类型")
    private String organizationType;

    @ApiModelProperty(value = "组织职能主键")
    private String relId;
}
