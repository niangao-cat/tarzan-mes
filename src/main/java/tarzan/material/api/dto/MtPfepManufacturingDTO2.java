package tarzan.material.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtPfepManufacturingDTO2 implements Serializable {
    private static final long serialVersionUID = -4222454245781958396L;

    @ApiModelProperty(value = "来源物料ID")
    private String sourceMaterialId;
    @ApiModelProperty(value = "来源物料类别ID")
    private String sourceMaterialCategoryId;
    @ApiModelProperty(value = "来源站点id", required = true)
    private String sourceSiteId;
    @ApiModelProperty(value = "来源组织ID")
    private String sourceOrgId;
    @ApiModelProperty(value = "来源组织类型")
    private String sourceOrgType;

    @ApiModelProperty(value = "目标物料ID")
    private String targetMaterialId;
    @ApiModelProperty(value = "目标物料类别ID")
    private String targetMaterialCategoryId;
    @ApiModelProperty(value = "目标站点id", required = true)
    private String targetSiteId;
    @ApiModelProperty(value = "目标组织ID")
    private String targetOrgId;
    @ApiModelProperty(value = "目标组织类型")
    private String targetOrgType;

    public String getSourceMaterialId() {
        return sourceMaterialId;
    }

    public void setSourceMaterialId(String sourceMaterialId) {
        this.sourceMaterialId = sourceMaterialId;
    }

    public String getSourceMaterialCategoryId() {
        return sourceMaterialCategoryId;
    }

    public void setSourceMaterialCategoryId(String sourceMaterialCategoryId) {
        this.sourceMaterialCategoryId = sourceMaterialCategoryId;
    }

    public String getSourceSiteId() {
        return sourceSiteId;
    }

    public void setSourceSiteId(String sourceSiteId) {
        this.sourceSiteId = sourceSiteId;
    }

    public String getSourceOrgId() {
        return sourceOrgId;
    }

    public void setSourceOrgId(String sourceOrgId) {
        this.sourceOrgId = sourceOrgId;
    }

    public String getSourceOrgType() {
        return sourceOrgType;
    }

    public void setSourceOrgType(String sourceOrgType) {
        this.sourceOrgType = sourceOrgType;
    }

    public String getTargetMaterialId() {
        return targetMaterialId;
    }

    public void setTargetMaterialId(String targetMaterialId) {
        this.targetMaterialId = targetMaterialId;
    }

    public String getTargetMaterialCategoryId() {
        return targetMaterialCategoryId;
    }

    public void setTargetMaterialCategoryId(String targetMaterialCategoryId) {
        this.targetMaterialCategoryId = targetMaterialCategoryId;
    }

    public String getTargetSiteId() {
        return targetSiteId;
    }

    public void setTargetSiteId(String targetSiteId) {
        this.targetSiteId = targetSiteId;
    }

    public String getTargetOrgId() {
        return targetOrgId;
    }

    public void setTargetOrgId(String targetOrgId) {
        this.targetOrgId = targetOrgId;
    }

    public String getTargetOrgType() {
        return targetOrgType;
    }

    public void setTargetOrgType(String targetOrgType) {
        this.targetOrgType = targetOrgType;
    }
}
