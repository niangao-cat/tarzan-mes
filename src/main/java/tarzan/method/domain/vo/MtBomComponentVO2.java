package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomComponentVO2 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3821693493915525946L;
    private String bomComponentId;
    private String materialId;
    private Double componentQty;

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

    public Double getComponentQty() {
        return componentQty;
    }

    public void setComponentQty(Double componentQty) {
        this.componentQty = componentQty;
    }

}
