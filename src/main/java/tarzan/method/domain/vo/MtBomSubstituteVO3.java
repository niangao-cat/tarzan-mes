package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomSubstituteVO3 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8996749615770923983L;
    private String materialId;
    private Double componentQty;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public Double getComponentQty() {
        return componentQty;
    }

    public void setComponentQty(Double componentQty) {
        this.componentQty = componentQty;
    }

}
