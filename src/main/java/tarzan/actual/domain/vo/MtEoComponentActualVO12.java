package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/3/18 13:35
 */
public class MtEoComponentActualVO12 implements Serializable {
    private static final long serialVersionUID = 2989004616712831589L;

    private String eoId;
    private String operationId;
    private String routerStepId;
    private String unstartFlag;

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
