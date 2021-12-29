package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/3/15 14:24
 */
public class MtEoComponentActualVO10 implements Serializable {
    private static final long serialVersionUID = -749694804642956251L;

    private String eoId;
    private String bomComponentId;
    private String materialId;
    private String componentType;
    private String routerStepId;
    private String operationId;
    private String substituteIncludedFlag;
    private String bomId;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
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

    public String getSubstituteIncludedFlag() {
        return substituteIncludedFlag;
    }

    public void setSubstituteIncludedFlag(String substituteIncludedFlag) {
        this.substituteIncludedFlag = substituteIncludedFlag;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }
}
