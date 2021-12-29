package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Objects;

public class MtEoComponentActualVO33 implements Serializable {
    private static final long serialVersionUID = 6522475667242855341L;
    private String eoId;
    private String materialId;
    private String componentType;
    private String operationId;
    private String issuedLocatorId;

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

    public String getIssuedLocatorId() {
        return issuedLocatorId;
    }

    public void setIssuedLocatorId(String issuedLocatorId) {
        this.issuedLocatorId = issuedLocatorId;
    }

    public MtEoComponentActualVO33(String eoId, String materialId, String componentType, String operationId, String issuedLocatorId) {
        this.eoId = eoId;
        this.materialId = materialId;
        this.componentType = componentType;
        this.operationId = operationId;
        this.issuedLocatorId = issuedLocatorId;
    }

    public MtEoComponentActualVO33() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtEoComponentActualVO33 that = (MtEoComponentActualVO33) o;
        return Objects.equals(getEoId(), that.getEoId()) && Objects.equals(getMaterialId(), that.getMaterialId())
                        && Objects.equals(getComponentType(), that.getComponentType())
                        && Objects.equals(getOperationId(), that.getOperationId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEoId(), getMaterialId(), getComponentType(), getOperationId());
    }
}


