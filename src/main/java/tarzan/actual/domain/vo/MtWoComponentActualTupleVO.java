package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: changbu  2020/10/31 13:25
 */
public class MtWoComponentActualTupleVO implements Serializable {


    private static final long serialVersionUID = -4947049362794682781L;
    private String workOrderId;
    private String materialId;
    private String operationId;
    private String componentType;
    private String bomComponentId;
    private String bomId;
    private String routerStepId;

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

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
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

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public MtWoComponentActualTupleVO(String workOrderId, String materialId, String operationId, String componentType, String bomComponentId, String bomId, String routerStepId) {
        this.workOrderId = workOrderId;
        this.materialId = materialId;
        this.operationId = operationId;
        this.componentType = componentType;
        this.bomComponentId = bomComponentId;
        this.bomId = bomId;
        this.routerStepId = routerStepId;
    }

    public MtWoComponentActualTupleVO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtWoComponentActualTupleVO that = (MtWoComponentActualTupleVO) o;
        return Objects.equals(workOrderId, that.workOrderId) &&
                Objects.equals(materialId, that.materialId) &&
                Objects.equals(operationId, that.operationId) &&
                Objects.equals(componentType, that.componentType) &&
                Objects.equals(bomComponentId, that.bomComponentId) &&
                Objects.equals(bomId, that.bomId) &&
                Objects.equals(routerStepId, that.routerStepId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workOrderId, materialId, operationId, componentType, bomComponentId, bomId, routerStepId);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append("workOrderId=").append(workOrderId);
        sb.append(", materialId=").append(materialId);
        sb.append(", operationId=").append(operationId);
        sb.append(", componentType='").append(componentType).append('\'');
        sb.append(", bomComponentId=").append(bomComponentId);
        sb.append(", bomId=").append(bomId);
        sb.append(", routerStepId=").append(routerStepId);
        sb.append('}');
        return sb.toString();
    }
}
