package tarzan.material.domain.vo;

import java.io.Serializable;

/**
 * @author : MrZ
 * @date : 2020-06-15 10:08
 **/
public class MtMaterialCategoryAssignVO8 implements Serializable {
    private static final long serialVersionUID = -6786911570586021208L;
    private String materialCategoryId;
    private String siteId;
    private String materialId;
    private String materialCategoryAssignId;

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

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialCategoryAssignId() {
        return materialCategoryAssignId;
    }

    public void setMaterialCategoryAssignId(String materialCategoryAssignId) {
        this.materialCategoryAssignId = materialCategoryAssignId;
    }
}
