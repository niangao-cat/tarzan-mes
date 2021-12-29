package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoRouterActualVO2 implements Serializable {

    private static final long serialVersionUID = -5242171794298167426L;
    private String eoId;
    private String operationId;
    private String stepName;
    private String routerStepId;
    private String sourceOperationId;
    private String sourceStepName;
    private String sourceRouterStepId;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
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

    public String getSourceOperationId() {
        return sourceOperationId;
    }

    public void setSourceOperationId(String sourceOperationId) {
        this.sourceOperationId = sourceOperationId;
    }

    public String getSourceStepName() {
        return sourceStepName;
    }

    public void setSourceStepName(String sourceStepName) {
        this.sourceStepName = sourceStepName;
    }

    public String getSourceRouterStepId() {
        return sourceRouterStepId;
    }

    public void setSourceRouterStepId(String sourceRouterStepId) {
        this.sourceRouterStepId = sourceRouterStepId;
    }
}
