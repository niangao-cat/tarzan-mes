package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * Created by HP on 2019/3/12.
 */
public class MtWoComponentActualVO20 implements Serializable {

    private static final long serialVersionUID = 4198249197891275343L;

    private String workOrderId;

    private String operationId;

    private String routerStepId;

    private String unstartFlag;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getUnstartFlag() {
        return unstartFlag;
    }

    public void setUnstartFlag(String unstartFlag) {
        this.unstartFlag = unstartFlag;
    }
}
