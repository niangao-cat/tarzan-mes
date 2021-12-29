package tarzan.order.domain.vo;

import java.io.Serializable;

/**
 * Created by slj on 2019-02-19.
 */
public class MtWorkOrderVO18 implements Serializable {
    private static final long serialVersionUID = 8699743084684543208L;
    private Double componentQty;
    private String materialId;
    private Double childQty;

    public Double getComponentQty() {
        return componentQty;
    }

    public void setComponentQty(Double componentQty) {
        this.componentQty = componentQty;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public Double getChildQty() {
        return childQty;
    }

    public void setChildQty(Double childQty) {
        this.childQty = childQty;
    }

}
