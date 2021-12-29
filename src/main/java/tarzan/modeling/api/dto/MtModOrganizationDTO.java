package tarzan.modeling.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class MtModOrganizationDTO implements Serializable {
    private static final long serialVersionUID = 2876036394565284377L;
    @ApiModelProperty(value = "被删除的组织的顶层站点ID", required = true)
    @NotBlank
    private String topSiteId;
    @ApiModelProperty(value = "被删除的组织的父组织类型", required = true)
    @NotBlank
    private String parentOrganizationType;
    @ApiModelProperty(value = "被删除的组织的父组织ID", required = true)
    @NotBlank
    private String parentOrganizationId;
    @ApiModelProperty(value = "被删除的组织类型", required = true)
    @NotBlank
    private String organizationType;
    @ApiModelProperty(value = "被删除的组织ID", required = true)
    @NotBlank
    private String organizationId;

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

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}
