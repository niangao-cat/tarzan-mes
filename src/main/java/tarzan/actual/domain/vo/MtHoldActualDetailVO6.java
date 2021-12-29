package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtHoldActualDetailVO6 implements Serializable {

    private static final long serialVersionUID = 985988482822166495L;

    private String eoId;
    private String routerStepId;// 工艺路线步骤ID
    private String status;// 工艺路线步骤ID

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
