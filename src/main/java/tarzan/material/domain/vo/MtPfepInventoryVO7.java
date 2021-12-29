package tarzan.material.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtPfepInventoryVO7
 * @description
 * @date 2019年10月11日 15:07
 */
public class MtPfepInventoryVO7 implements Serializable {
    private static final long serialVersionUID = -9219884710203531713L;
    private String materialId; // 物料ID
    private String siteId; // 站点ID
    private String organizationType; // 组织类型
    private String organizationId; // 组织ID
    private String materialSiteId;// 物料站点ID

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
}
