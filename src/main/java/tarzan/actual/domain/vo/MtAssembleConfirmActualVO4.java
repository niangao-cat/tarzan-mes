package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author MrZ
 */
public class MtAssembleConfirmActualVO4 implements Serializable {

    private static final long serialVersionUID = -5038853769575549778L;
    private String assembleConfirmActualId;// 装配确认实绩ID
    private String materialId;// 物料
    private String operationId;// 工艺
    private String bomComponentId;// 组件
    private String routerStepId;// 工艺路线步骤
    private String bypassBy;// 放行人

    public String getAssembleConfirmActualId() {
        return assembleConfirmActualId;
    }

    public void setAssembleConfirmActualId(String assembleConfirmActualId) {
        this.assembleConfirmActualId = assembleConfirmActualId;
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

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getBypassBy() {
        return bypassBy;
    }

    public void setBypassBy(String bypassBy) {
        this.bypassBy = bypassBy;
    }
}
