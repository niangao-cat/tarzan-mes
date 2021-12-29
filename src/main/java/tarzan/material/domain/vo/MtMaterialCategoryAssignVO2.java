package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtMaterialCategoryAssignVO2 implements Serializable {
    private static final long serialVersionUID = 9172189598172037913L;

    @ApiModelProperty("物料Id")
    private String materialId;
    @ApiModelProperty("站点Id")
    private String siteId;
    @ApiModelProperty("物料站点关系Id")
    private String materialSiteId;
    @ApiModelProperty("物料类别Id")
    private String materialCategoryId;

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

    public String getMaterialSiteId() {
        return materialSiteId;
    }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
    }

    public String getMaterialCategoryId() {
        return materialCategoryId;
    }

    public void setMaterialCategoryId(String materialCategoryId) {
        this.materialCategoryId = materialCategoryId;
    }
}
