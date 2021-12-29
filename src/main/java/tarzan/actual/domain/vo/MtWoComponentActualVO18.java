package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * Created by sen.luo on 2019/3/11.
 */
public class MtWoComponentActualVO18 implements Serializable {

    private static final long serialVersionUID = -4550119626091213471L;

    private String workOrderId;

    private String materialId;

    private String componentType;

    private String operationId;

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

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
