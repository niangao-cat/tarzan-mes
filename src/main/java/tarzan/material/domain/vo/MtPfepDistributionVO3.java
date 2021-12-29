package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * MtPfepDistributionVO3
 *
 * @author: {xieyiyang}
 * @date: 2020/2/4 22:26
 * @description:
 */
public class MtPfepDistributionVO3 implements Serializable {
    private static final long serialVersionUID = 6500907772848820054L;

    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "组织类型")
    private String organizationType;
    @ApiModelProperty(value = "组织ID")
    private String organizationId;
    @ApiModelProperty(value = "物料站点ID")
    private String materialSiteId;
    @ApiModelProperty(value = "配送路线ID")
    private String areaId;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
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

    public String getMaterialSiteId() {
        return materialSiteId;
    }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
