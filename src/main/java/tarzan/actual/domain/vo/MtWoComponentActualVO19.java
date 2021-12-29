package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * Created by HP on 2019/3/12.
 */
public class MtWoComponentActualVO19 implements Serializable {

    private static final long serialVersionUID = 4399171720573336266L;
    private String bomId;
    private String workOrderId;

    private String materialId;

    private String componentType;

    private String operationId;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
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

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
