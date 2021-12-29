package tarzan.actual.domain.vo;

import java.io.Serializable;

import com.google.common.base.Objects;

public class MtEoComponentActualVO implements Serializable {
    private static final long serialVersionUID = 6522475667242855341L;
    private String eoId;
    private String materialId;
    private String componentType;
    private String operationId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public MtEoComponentActualVO(String eoId, String materialId, String componentType, String operationId) {
        this.eoId = eoId;
        this.materialId = materialId;
        this.componentType = componentType;
        this.operationId = operationId;
    }

    public MtEoComponentActualVO() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtEoComponentActualVO that = (MtEoComponentActualVO) o;
        return Objects.equal(eoId, that.eoId) && Objects.equal(materialId, that.materialId)
                        && Objects.equal(componentType, that.componentType)
                        && Objects.equal(operationId, that.operationId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(eoId, materialId, componentType, operationId);
    }
}
