package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Tangxiao
 */
public class MtAssembleConfirmActualVO25 implements Serializable {

    private static final long serialVersionUID = 6429647846393089248L;
    private String assembleConfirmActualId;
    private String eoId;
    private String materialId;
    private String operationId;
    private String componentType;
    private String bomComponentId;
    private String bomId;
    private String routerStepId;
    private String assembleExcessFlag;
    private String assembleRouterType;
    private String substituteFlag;

    public MtAssembleConfirmActualVO25() {
    }

    public MtAssembleConfirmActualVO25(String eoId, String materialId, String operationId, String componentType, String bomComponentId, String bomId, String routerStepId) {
        this.eoId = eoId;
        this.materialId = materialId;
        this.operationId = operationId;
        this.componentType = componentType;
        this.bomComponentId = bomComponentId;
        this.bomId = bomId;
        this.routerStepId = routerStepId;
    }

    public String getAssembleConfirmActualId() {
        return assembleConfirmActualId;
    }

    public void setAssembleConfirmActualId(String assembleConfirmActualId) {
        this.assembleConfirmActualId = assembleConfirmActualId;
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

    public String getAssembleExcessFlag() {
        return assembleExcessFlag;
    }

    public void setAssembleExcessFlag(String assembleExcessFlag) {
        this.assembleExcessFlag = assembleExcessFlag;
    }

    public String getAssembleRouterType() {
        return assembleRouterType;
    }

    public void setAssembleRouterType(String assembleRouterType) {
        this.assembleRouterType = assembleRouterType;
    }

    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtAssembleConfirmActualVO25 that = (MtAssembleConfirmActualVO25) o;
        return Objects.equals(eoId, that.eoId) && Objects.equals(materialId, that.materialId)
                        && Objects.equals(operationId, that.operationId)
                        && Objects.equals(componentType, that.componentType)
                        && Objects.equals(bomComponentId, that.bomComponentId) && Objects.equals(bomId, that.bomId)
                        && Objects.equals(routerStepId, that.routerStepId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eoId, materialId, operationId, componentType, bomComponentId, bomId, routerStepId);
    }
}


