package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepWipVO7 implements Serializable {
    private static final long serialVersionUID = 6237878295358419515L;

    private String workcellId; // 工作单元
    private String status; // 状态
    private String operationId; // 工艺
    private String stepName; // 步骤识别码

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
