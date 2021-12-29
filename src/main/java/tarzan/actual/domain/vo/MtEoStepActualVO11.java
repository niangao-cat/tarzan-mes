package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepActualVO11 implements Serializable {
    private static final long serialVersionUID = 1833439812855141451L;

    private String sourceEoStepActualId;
    private String eoRouterActualId;
    private String routerId;
    private String returnType;
    private String operationId;
    private String stepName;
    private String sourceRouterStepId;

    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
    }

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
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

    public String getSourceRouterStepId() {
        return sourceRouterStepId;
    }

    public void setSourceRouterStepId(String sourceRouterStepId) {
        this.sourceRouterStepId = sourceRouterStepId;
    }
}
