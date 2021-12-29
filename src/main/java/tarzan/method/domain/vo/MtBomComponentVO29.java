package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomComponentVO29 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 64105474440577231L;
    private String bomComponentId;
    private Double componentQty;

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public Double getComponentQty() {
        return componentQty;
    }

    public void setComponentQty(Double componentQty) {
        this.componentQty = componentQty;
    }

}
