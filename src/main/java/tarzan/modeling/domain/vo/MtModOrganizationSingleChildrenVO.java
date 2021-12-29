package tarzan.modeling.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by slj on 2018-09-06.
 */
public class MtModOrganizationSingleChildrenVO implements Serializable {
    private static final long serialVersionUID = 5908912041490423460L;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTopSiteId() {
        return topSiteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTopSiteId(String topSiteId) {
        this.topSiteId = topSiteId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public Boolean isChildren() {
        return children;
    }

    public void setChildren(Boolean children) {
        this.children = children;
    }
}
