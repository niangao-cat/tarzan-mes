package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author Tangxiao
 */
public class MtAssembleConfirmActualTupleVO implements Serializable {

    private static final long serialVersionUID = -3012301562896917855L;
    private String eoId;
    private String materialId;
    private String operationId;
    private String componentType;
    private String bomComponentId;
    private String bomId;
    private String routerStepId;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MtAssembleConfirmActualTupleVO that = (MtAssembleConfirmActualTupleVO) o;

        if (eoId != null ? !eoId.equals(that.eoId) : that.eoId != null) return false;
        if (materialId != null ? !materialId.equals(that.materialId) : that.materialId != null) return false;
        if (operationId != null ? !operationId.equals(that.operationId) : that.operationId != null) return false;
        if (componentType != null ? !componentType.equals(that.componentType) : that.componentType != null)
            return false;
        if (bomComponentId != null ? !bomComponentId.equals(that.bomComponentId) : that.bomComponentId != null)
            return false;
        if (bomId != null ? !bomId.equals(that.bomId) : that.bomId != null) return false;
        return routerStepId != null ? routerStepId.equals(that.routerStepId) : that.routerStepId == null;
    }

    @Override
    public int hashCode() {
        int result = eoId != null ? eoId.hashCode() : 0;
        result = 31 * result + (materialId != null ? materialId.hashCode() : 0);
        result = 31 * result + (operationId != null ? operationId.hashCode() : 0);
        result = 31 * result + (componentType != null ? componentType.hashCode() : 0);
        result = 31 * result + (bomComponentId != null ? bomComponentId.hashCode() : 0);
        result = 31 * result + (bomId != null ? bomId.hashCode() : 0);
        result = 31 * result + (routerStepId != null ? routerStepId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "eoId=" + eoId +
                ", materialId=" + materialId +
                ", operationId=" + operationId +
                ", componentType='" + componentType + '\'' +
                ", bomComponentId=" + bomComponentId +
                ", bomId=" + bomId +
                ", routerStepId=" + routerStepId +
                '}';
    }
}


