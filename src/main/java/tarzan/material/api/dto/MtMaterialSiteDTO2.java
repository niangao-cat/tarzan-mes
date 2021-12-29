package tarzan.material.api.dto;

import java.io.Serializable;

public class MtMaterialSiteDTO2 implements Serializable {

    private static final long serialVersionUID = 6717603876001093623L;

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

    private String siteId;
    private String materialSiteId;
}
