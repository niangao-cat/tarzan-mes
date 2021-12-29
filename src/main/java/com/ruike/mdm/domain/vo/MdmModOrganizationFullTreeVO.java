package com.ruike.mdm.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * description
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/6 16:32
 */
@Data
public class MdmModOrganizationFullTreeVO {
    @ApiModelProperty("组织ID")
    private String id;

    @ApiModelProperty("组织编码")
    private String code;

    @ApiModelProperty("组织名称")
    private String name;

    @ApiModelProperty("组织描述")
    private String text;

    @ApiModelProperty("组织类型")
    private String type;

    @ApiModelProperty("顶层站点ID")
    private String topSiteId;

    @ApiModelProperty("顺序")
    private Long sequence;

    @ApiModelProperty("父组织ID")
    private String parentId;

    @ApiModelProperty("父组织类型")
    private String parentType;

    @ApiModelProperty("是否有子节点")
    private Boolean children;

    @ApiModelProperty("子节点列表")
    private List<MdmModOrganizationFullTreeVO> childrenList;
}
