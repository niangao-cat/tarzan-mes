package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Objects;

public class MtRouterOpComponentVO3 implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -1506963602215482101L;
    private String bomId;
    private String materialId;
    private String componentType;
    private String routerId;
    private String operationId;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
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

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public MtRouterOpComponentVO3(String bomId, String materialId, String componentType, String routerId,
                    String operationId) {
        this.bomId = bomId;
        this.materialId = materialId;
        this.componentType = componentType;
        this.routerId = routerId;
        this.operationId = operationId;
    }

    public MtRouterOpComponentVO3() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtRouterOpComponentVO3 that = (MtRouterOpComponentVO3) o;
        return Objects.equals(getBomId(), that.getBomId()) && Objects.equals(getMaterialId(), that.getMaterialId())
                        && Objects.equals(getComponentType(), that.getComponentType())
                        && Objects.equals(getRouterId(), that.getRouterId())
                        && Objects.equals(getOperationId(), that.getOperationId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBomId(), getMaterialId(), getComponentType(), getRouterId(), getOperationId());
    }
}
