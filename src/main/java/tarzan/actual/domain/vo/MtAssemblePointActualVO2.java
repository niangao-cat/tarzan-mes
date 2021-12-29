package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtAssemblePointActualVO2 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5826173266135103114L;
    private String assemblePointId;
    private String materialId;

    public String getAssemblePointId() {
        return assemblePointId;
    }

    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

}
