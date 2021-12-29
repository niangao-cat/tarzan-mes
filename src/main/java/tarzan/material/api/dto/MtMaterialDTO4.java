package tarzan.material.api.dto;

import java.io.Serializable;

public class MtMaterialDTO4 implements Serializable {
    private static final long serialVersionUID = -8557443635460003666L;
    private String materialId;
    private String primaryUomId;
    private String secondaryUomId;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getPrimaryUomId() {
        return primaryUomId;
    }

    public void setPrimaryUomId(String primaryUomId) {
        this.primaryUomId = primaryUomId;
    }

    public String getSecondaryUomId() {
        return secondaryUomId;
    }

    public void setSecondaryUomId(String secondaryUomId) {
        this.secondaryUomId = secondaryUomId;
    }
}
