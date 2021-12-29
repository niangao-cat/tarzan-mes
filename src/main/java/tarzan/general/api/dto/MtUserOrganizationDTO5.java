package tarzan.general.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtUserOrganizationDTO5 implements Serializable {


    private static final long serialVersionUID = -5858173754270630138L;
    @ApiModelProperty(value = "组织类型", required = true)
    private String organizationType;

    @ApiModelProperty(value = "组织编码")
    private String organizationCode;

    @ApiModelProperty(value = "组织描述")
    private String organizationDesc;

    @ApiModelProperty(value = "站点ID")
    private String siteId;

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationDesc() {
        return organizationDesc;
    }

    public void setOrganizationDesc(String organizationDesc) {
        this.organizationDesc = organizationDesc;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
}


