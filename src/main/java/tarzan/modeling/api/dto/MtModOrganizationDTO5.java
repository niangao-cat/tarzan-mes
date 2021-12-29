package tarzan.modeling.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtModOrganizationDTO5 implements Serializable {
    private static final long serialVersionUID = -4885658669894793510L;

    @ApiModelProperty(value = "选中组织的顶层站点ID")
    private String topSiteId;
    @ApiModelProperty(value = "选中组织的父组织类型", required = true)
    private String parentOrganizationType;
    @ApiModelProperty(value = "选中组织的父组织ID", required = true)
    private String parentOrganizationId;
    @ApiModelProperty(value = "选中组织的组织类型", required = true)
    private String organizationType;

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

}
