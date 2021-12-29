package tarzan.material.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * TODO
 *
 * @author jin.xu@hand-china.com
 * @date 2020/2/12 17:04
 */
public class MtPfepDistributionDTO3 implements Serializable {
    private static final long serialVersionUID = 363930855336728397L;

    @ApiModelProperty(value = "来源-物料ID")
    private String sourceMaterialId;

    @ApiModelProperty(value = "来源-物料类别ID")
    private String sourceMaterialCategoryId;

    @ApiModelProperty(value = "来源-生产站点ID/物料站点ID/分类集站点ID")
    private String sourceSiteId;

    @ApiModelProperty(value = "来源-配送路线ID")
    private String sourceAreaId;

    @ApiModelProperty(value = "来源-线边库位ID")
    private String sourceLocatorId;

    @ApiModelProperty(value = "来源-工作单元ID")
    private String sourceWorkcellId;

    @ApiModelProperty(value = "目标-物料ID")
    private String targetMaterialId;

    @ApiModelProperty(value = "目标-物料类别ID")
    private String targetMaterialCategoryId;

    @ApiModelProperty(value = "目标-生产站点ID/物料站点ID/分类集站点ID")
    private String targetSiteId;

    @ApiModelProperty(value = "目标-配送路线ID")
    private String targetAreaId;

    @ApiModelProperty(value = "目标-线边库位ID")
    private String targetLocatorId;

    @ApiModelProperty(value = "目标-工作单元ID")
    private String targetWorkcellId;

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

    public String getSourceAreaId() {
        return sourceAreaId;
    }

    public void setSourceAreaId(String sourceAreaId) {
        this.sourceAreaId = sourceAreaId;
    }

    public String getSourceLocatorId() {
        return sourceLocatorId;
    }

    public void setSourceLocatorId(String sourceLocatorId) {
        this.sourceLocatorId = sourceLocatorId;
    }

    public String getSourceWorkcellId() {
        return sourceWorkcellId;
    }

    public void setSourceWorkcellId(String sourceWorkcellId) {
        this.sourceWorkcellId = sourceWorkcellId;
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

    public String getTargetAreaId() {
        return targetAreaId;
    }

    public void setTargetAreaId(String targetAreaId) {
        this.targetAreaId = targetAreaId;
    }

    public String getTargetLocatorId() {
        return targetLocatorId;
    }

    public void setTargetLocatorId(String targetLocatorId) {
        this.targetLocatorId = targetLocatorId;
    }

    public String getTargetWorkcellId() {
        return targetWorkcellId;
    }

    public void setTargetWorkcellId(String targetWorkcellId) {
        this.targetWorkcellId = targetWorkcellId;
    }
}
