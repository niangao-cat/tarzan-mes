package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * Created by HP on 2019/3/12.
 */
public class MtWoComponentActualVO5 implements Serializable {

    private static final long serialVersionUID = -9194177530705266746L;

    private String workOrderId;
    private String bomId;
    private String bomComponentId;
    private String materialId;
    private String componentType;
    private String routerStepId;
    private String operationId;
    private String substituteIncludedFlag;

    public String getSubstituteIncludedFlag() {
        return substituteIncludedFlag;
    }

    public void setSubstituteIncludedFlag(String substituteIncludedFlag) {
        this.substituteIncludedFlag = substituteIncludedFlag;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
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

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
