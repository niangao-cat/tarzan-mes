package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoRouterActualVO3 implements Serializable {
    private static final long serialVersionUID = 8915505723174634021L;
    private String eoId;
    private String workcellId;
    private String operationId;
    private String stepName;
    private String sourceOperationId;
    private String sourceStepName;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
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
}
