package tarzan.dispatch.domain.vo;

import java.io.Serializable;

public class MtEoDispatchActionVO16 implements Serializable {


    /**
     * 
     */
    private static final long serialVersionUID = -8036356930361495790L;
    private String routerStepId;
    private String eoId;

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
