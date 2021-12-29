package tarzan.method.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/3/21 20:28
 */
public class MtRouterStepVO3 implements Serializable {

    private static final long serialVersionUID = -3090808954536001319L;

    private String routerId;
    private String operationId;
    private String routerStepType;
    private String stepName;
    private String routerStepId;
    private String routerOperationId;
    private String bomComponentId;
    private String routerOperationComponentId;
    private String sequence;

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

    public String getRouterStepType() {
        return routerStepType;
    }

    public void setRouterStepType(String routerStepType) {
        this.routerStepType = routerStepType;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getRouterOperationId() {
        return routerOperationId;
    }

    public void setRouterOperationId(String routerOperationId) {
        this.routerOperationId = routerOperationId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getRouterOperationComponentId() {
        return routerOperationComponentId;
    }

    public void setRouterOperationComponentId(String routerOperationComponentId) {
        this.routerOperationComponentId = routerOperationComponentId;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}
