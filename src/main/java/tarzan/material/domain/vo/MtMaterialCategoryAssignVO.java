package tarzan.material.domain.vo;

import java.io.Serializable;

/**
 * Created by slj on 2018-11-21.
 */
public class MtMaterialCategoryAssignVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4961891278207207114L;
    private String siteId;
    private String materialId;
    private String defaultType;
    private String materialCategorySetId;
    private String materialSiteId;
    private String materialCategoryId;
    private String materialCategoryAssignId;

    public String getMaterialCategoryAssignId() {
        return materialCategoryAssignId;
    }

    public void setMaterialCategoryAssignId(String materialCategoryAssignId) {
        this.materialCategoryAssignId = materialCategoryAssignId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getDefaultType() {
        return defaultType;
    }

    public void setDefaultType(String defaultType) {
        this.defaultType = defaultType;
    }

    public String getMaterialCategorySetId() {
        return materialCategorySetId;
    }

    public void setMaterialCategorySetId(String materialCategorySetId) {
        this.materialCategorySetId = materialCategorySetId;
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
