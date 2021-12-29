package tarzan.material.api.dto;

import java.io.Serializable;

public class MtMaterialSiteDTO3 implements Serializable {
    private static final long serialVersionUID = 7184963918517610536L;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialSiteId() {
        return materialSiteId;
    }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
    }

    private String materialId;
    private String materialSiteId;
}
