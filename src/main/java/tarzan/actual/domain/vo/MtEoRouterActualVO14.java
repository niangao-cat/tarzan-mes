package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoRouterActualVO14 implements Serializable {
    private static final long serialVersionUID = -251607692534185281L;

    private String eoRouterActualId; // 工艺路线实绩
    private String routerStepId; // 步骤唯一标识

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }
}
