package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * Created by HP on 2019/3/12.
 */
public class MtWoComponentActualVO6 implements Serializable {

    private static final long serialVersionUID = -2010903925795986911L;
    private String materialId;
    private String componentType;
    private String bomComponentId;
    private Double unassembledQty;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public Double getUnassembledQty() {
        return unassembledQty;
    }

    public void setUnassembledQty(Double unassembledQty) {
        this.unassembledQty = unassembledQty;
    }
}
