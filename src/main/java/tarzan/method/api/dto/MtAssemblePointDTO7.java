package tarzan.method.api.dto;

import java.io.Serializable;

public class MtAssemblePointDTO7 implements Serializable {
    private static final long serialVersionUID = -3082658050374247149L;

    private String assembleGroupId;
    private String materialId;

    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }
}
