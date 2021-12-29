package tarzan.modeling.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModOrganizationRelDTO5 implements Serializable {
    private static final long serialVersionUID = -4938731500573193943L;

    @ApiModelProperty(value = "顶层站点ID", required = true)
    private String topSiteId;
    @ApiModelProperty(value = "父层关系对象类型", required = true)
    private String parentOrganizationType;
    @ApiModelProperty(value = "父层关系对象", required = true)
    private String parentOrganizationId;
    @ApiModelProperty(value = "子层对象类型", required = true)
    private String organizationType;
    @ApiModelProperty(value = "子层对象", required = true)
    private String organizationId;


    /**
     * @return 顶层站点ID
     */
    public String getTopSiteId() {
        return topSiteId;
    }

    public void setTopSiteId(String topSiteId) {
        this.topSiteId = topSiteId;
    }

    /**
     * @return 父层关系对象类型
     */
    public String getParentOrganizationType() {
        return parentOrganizationType;
    }

    public void setParentOrganizationType(String parentOrganizationType) {
        this.parentOrganizationType = parentOrganizationType;
    }

    /**
     * @return 父层关系对象
     */
    public String getParentOrganizationId() {
        return parentOrganizationId;
    }

    public void setParentOrganizationId(String parentOrganizationId) {
        this.parentOrganizationId = parentOrganizationId;
    }

    /**
     * @return 子层对象类型
     */
    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    /**
     * @return 子层对象
     */
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}
