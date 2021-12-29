package tarzan.method.api.dto;

import java.io.Serializable;

public class MtBomComponentDTO implements Serializable {
    
    private static final long serialVersionUID = -6264762076133733614L;
    private String siteId;
    private String materialId;
    private String bomComponentType;
    
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
    public String getBomComponentType() {
        return bomComponentType;
    }
    public void setBomComponentType(String bomComponentType) {
        this.bomComponentType = bomComponentType;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtBomComponentDTO [siteId=");
        builder.append(siteId);
        builder.append(", materialId=");
        builder.append(materialId);
        builder.append(", bomComponentType=");
        builder.append(bomComponentType);
        builder.append("]");
        return builder.toString();
    }
    
}
