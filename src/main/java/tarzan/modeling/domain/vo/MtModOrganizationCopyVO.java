package tarzan.modeling.domain.vo;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by slj on 2018-11-22.
 */
public class MtModOrganizationCopyVO implements Serializable {

    private static final long serialVersionUID = -2834751694254892400L;
    @ApiModelProperty(value = "被复制的组织的顶层站点ID", required = true)
    @NotBlank
    private String topSiteId;
    @ApiModelProperty(value = "被复制的组织的父组织类型", required = true)
    @NotBlank
    private String parentOrganizationType;
    @ApiModelProperty(value = "被复制的组织的父组织ID", required = true)
    @NotBlank
    private String parentOrganizationId;
    @ApiModelProperty(value = "被复制的组织类型", required = true)
    @NotBlank
    private String organizationType;
    @ApiModelProperty(value = "被复制的组织ID", required = true)
    @NotBlank
    private String organizationId;
    @ApiModelProperty(value = "复制到的组织的顶层站点ID", required = true)
    @NotBlank
    private String targetSiteId;
    @ApiModelProperty(value = "复制到的组织类型", required = true)
    @NotBlank
    private String targetOrganizationType;
    @ApiModelProperty(value = "复制到的组织ID", required = true)
    @NotBlank
    private String targetOrganizationId;


    public String getParentOrganizationId() {
        return parentOrganizationId;
    }

    public void setParentOrganizationId(String parentOrganizationId) {
        this.parentOrganizationId = parentOrganizationId;
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


    public String getTargetSiteId() {
        return targetSiteId;
    }

    public void setTargetSiteId(String targetSiteId) {
        this.targetSiteId = targetSiteId;
    }

    public String getTargetOrganizationType() {
        return targetOrganizationType;
    }

    public void setTargetOrganizationType(String targetOrganizationType) {
        this.targetOrganizationType = targetOrganizationType;
    }

    public String getTargetOrganizationId() {
        return targetOrganizationId;
    }

    public void setTargetOrganizationId(String targetOrganizationId) {
        this.targetOrganizationId = targetOrganizationId;
    }


}
