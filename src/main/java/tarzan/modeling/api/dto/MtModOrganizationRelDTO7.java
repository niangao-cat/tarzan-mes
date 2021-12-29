package tarzan.modeling.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 根据站点和组织 查询站点或组织下树结构 传入参数DTO
 * 
 * @author benjamin
 */
public class MtModOrganizationRelDTO7 implements Serializable {
    private static final long serialVersionUID = -7830661559672863345L;

    @ApiModelProperty(value = "站点Id")
    private String topSiteId;

    @ApiModelProperty(value = "组织Id")
    private String parentOrganizationType;

    @ApiModelProperty(value = "组织类型")
    private String parentOrganizationId;

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
