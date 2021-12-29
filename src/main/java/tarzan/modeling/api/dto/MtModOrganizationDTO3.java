package tarzan.modeling.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtModOrganizationDTO3 implements Serializable {
    private static final long serialVersionUID = -2701437345499926751L;
    @ApiModelProperty(value = "LOV中的查询条件，组织编码")
    private String organizationCode;
    @ApiModelProperty(value = "LOV中的查询条件，组织描述")
    private String organizationName;
    @ApiModelProperty(value = "查询的组织类型", required = true)
    private String organizationType;
    @ApiModelProperty(value = "父节点的顶层站点ID", required = true)
    private String topSiteId;
    @ApiModelProperty(value = "父节点的父组织类型", required = true)
    private String parentOrganizationType;
    @ApiModelProperty(value = "被删除的组织的父组织ID", required = true)
    private String parentOrganizationId;

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getTopSiteId() {
        return topSiteId;
    }

    public void setTopSiteId(String topSiteId) {
        this.topSiteId = topSiteId;
    }

    public String getParentOrganizationType() {
        return parentOrganizationType;
    }

    public void setParentOrganizationType(String parentOrganizationType) {
        this.parentOrganizationType = parentOrganizationType;
    }

    public String getParentOrganizationId() {
        return parentOrganizationId;
    }

    public void setParentOrganizationId(String parentOrganizationId) {
        this.parentOrganizationId = parentOrganizationId;
    }
}
