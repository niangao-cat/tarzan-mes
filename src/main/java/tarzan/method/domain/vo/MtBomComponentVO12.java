package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomComponentVO12 implements Serializable {

    private static final long serialVersionUID = 7010046388058688221L;

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
