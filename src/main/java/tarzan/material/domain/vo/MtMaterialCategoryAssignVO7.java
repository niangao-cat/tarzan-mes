package tarzan.material.domain.vo;

import java.io.Serializable;

/**
 * @author : MrZ
 * @date : 2020-06-12 17:26
 **/
public class MtMaterialCategoryAssignVO7 implements Serializable {
    private static final long serialVersionUID = -1531409190183348347L;
    private String siteId;
    private String materialCategoryId;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getMaterialCategoryId() {
        return materialCategoryId;
    }

    public void setMaterialCategoryId(String materialCategoryId) {
        this.materialCategoryId = materialCategoryId;
    }
}
