package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 2020-02-06
 */
public class MtProcessWorkingStrategyOrgTreeDto implements Serializable {
    private static final long serialVersionUID = 8391987800276053822L;

    @ApiModelProperty("组织ID")
    private String id;

    @ApiModelProperty("组织编码")
    private String code;

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

    @ApiModelProperty("是否已存在关系")
    private Boolean alreadyDefined;

    @ApiModelProperty("是否被同功能其他配置定义")
    private Boolean isOtherStrategyDef;

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

    public Boolean getChildren() {
        return children;
    }

    public void setChildren(Boolean children) {
        this.children = children;
    }

    public Boolean getAlreadyDefined() {
        return alreadyDefined;
    }

    public void setAlreadyDefined(Boolean alreadyDefined) {
        this.alreadyDefined = alreadyDefined;
    }

    public Boolean getOtherStrategyDef() {
        return isOtherStrategyDef;
    }

    public void setOtherStrategyDef(Boolean otherStrategyDef) {
        this.isOtherStrategyDef = otherStrategyDef;
    }
}
