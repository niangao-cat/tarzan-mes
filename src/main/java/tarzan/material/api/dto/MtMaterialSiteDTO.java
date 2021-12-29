package tarzan.material.api.dto;

import java.io.Serializable;

public class MtMaterialSiteDTO implements Serializable {

private static final long serialVersionUID = -1571720481353113545L;

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

private String materialId;
private String siteId;
}
