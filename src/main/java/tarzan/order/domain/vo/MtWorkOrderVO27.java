package tarzan.order.domain.vo;

import com.google.common.base.Objects;

import java.io.Serializable;

public class MtWorkOrderVO27 implements Serializable {

    private static final long serialVersionUID = 837710982503208386L;
    private String workOrderId;
    private String materialId;
    private String componentType;
    private String operationId;

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

    public MtWorkOrderVO27() {
    }

    public MtWorkOrderVO27(String workOrderId, String materialId, String componentType, String operationId) {
        this.workOrderId = workOrderId;
        this.materialId = materialId;
        this.componentType = componentType;
        this.operationId = operationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MtWorkOrderVO27 that = (MtWorkOrderVO27) o;
        return Objects.equal(workOrderId, that.workOrderId) &&
                Objects.equal(materialId, that.materialId) &&
                Objects.equal(componentType, that.componentType) &&
                Objects.equal(operationId, that.operationId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(workOrderId, materialId, componentType, operationId);
    }
}
