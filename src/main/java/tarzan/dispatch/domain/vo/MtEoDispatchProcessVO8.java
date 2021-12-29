package tarzan.dispatch.domain.vo;

import java.io.Serializable;

public class MtEoDispatchProcessVO8 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3028611819009194851L;
    private String routerStepId; // 工作单元ID
    private String eoId; // 站点

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

}
