package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * Created by sen.luo on 2019/3/11.
 */
public class MtWoComponentActualVO17 implements Serializable {

    private static final long serialVersionUID = -677947817151176483L;

    private String workOrderId;

    private String materialId;

    private String componentType;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

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
}
