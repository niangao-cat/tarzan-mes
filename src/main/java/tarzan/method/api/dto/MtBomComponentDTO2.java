package tarzan.method.api.dto;

import java.io.Serializable;

public class MtBomComponentDTO2 implements Serializable {
    
    private static final long serialVersionUID = 6837663668920113904L;
    private String bomId;
    private String materialId;
    public String getBomId() {
        return bomId;
    }
    public void setBomId(String bomId) {
        this.bomId = bomId;
    }
    public String getMaterialId() {
        return materialId;
    }
    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtBomComponentDTO2 [bomId=");
        builder.append(bomId);
        builder.append(", materialId=");
        builder.append(materialId);
        builder.append("]");
        return builder.toString();
    }
}
