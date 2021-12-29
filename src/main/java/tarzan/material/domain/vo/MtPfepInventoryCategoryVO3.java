package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * MtPfepInventoryCategoryVO3
 *
 * @author: {xieyiyang}
 * @date: 2020/2/6 16:10
 * @description:
 */
public class MtPfepInventoryCategoryVO3 implements Serializable {
    private static final long serialVersionUID = 4627213137769489032L;

    @ApiModelProperty(value = "物料类别ID")
    private String materialCategoryId;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "组织类型")
    private String organizationType;
    @ApiModelProperty(value = "组织ID")
    private String organizationId;
    @ApiModelProperty(value = "物料类别站点ID")
    private String materialCategorySiteId;
    @ApiModelProperty(value = "配送路线ID")
    private String areaId;

    public String getMaterialCategoryId() {
        return materialCategoryId;
    }

    public void setMaterialCategoryId(String materialCategoryId) {
        this.materialCategoryId = materialCategoryId;
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

    public String getMaterialCategorySiteId() {
        return materialCategorySiteId;
    }

    public void setMaterialCategorySiteId(String materialCategorySiteId) {
        this.materialCategorySiteId = materialCategorySiteId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
