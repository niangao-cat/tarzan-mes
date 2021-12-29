package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtRouterVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3608842419999989935L;
    private String childStepId;
    private String operationId;

    public String getChildStepId() {
        return childStepId;
    }

    public void setChildStepId(String childStepId) {
        this.childStepId = childStepId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

}
