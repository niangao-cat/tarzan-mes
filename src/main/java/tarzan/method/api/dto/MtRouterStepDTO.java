package tarzan.method.api.dto;

import java.io.Serializable;

public class MtRouterStepDTO implements Serializable {
    private static final long serialVersionUID = -6718807839790898739L;

    private String routerId;
    private String routerStepId;

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }
}
