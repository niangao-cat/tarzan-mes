package tarzan.material.domain.vo;

import java.io.Serializable;

/**
 * @author : MrZ
 * @date : 2020-06-15 14:25
 **/
public class MtMaterialSiteVO5 implements Serializable {
    private static final long serialVersionUID = 952160362593813399L;
    private String materialId;
    private String materialSiteId;

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
}
