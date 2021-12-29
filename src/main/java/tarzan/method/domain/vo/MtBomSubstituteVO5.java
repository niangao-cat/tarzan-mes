package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomSubstituteVO5 implements Serializable {
    private static final long serialVersionUID = -1575492089596626617L;
    private String bomComponentId;
    private String materialId;

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }
}
