package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/4/10 14:35
 */
public class MtEoStepActualVO27 implements Serializable {

    private static final long serialVersionUID = 8786260851655074610L;

    private String operationId;
    private String stepName;

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
