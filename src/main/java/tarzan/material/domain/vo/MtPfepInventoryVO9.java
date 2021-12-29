package tarzan.material.domain.vo;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2019/7/23 10:08
 * @Description:
 */
public class MtPfepInventoryVO9 implements Serializable {
    private static final long serialVersionUID = 7489929035808098975L;

    private String materialId;
    private String siteId;
    private String organizationType;
    private String organizationId;
    private String identifyType;

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

    public String getIdentifyType() {
        return identifyType;
    }

    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }
}


