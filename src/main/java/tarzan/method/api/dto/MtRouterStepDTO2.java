package tarzan.method.api.dto;

import java.io.Serializable;

public class MtRouterStepDTO2 implements Serializable {
    private static final long serialVersionUID = 3982927774095052407L;

    private String routerId;
    private String operationId;

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
}
