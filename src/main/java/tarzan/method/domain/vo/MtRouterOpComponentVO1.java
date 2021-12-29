package tarzan.method.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/3/20 14:52
 */
public class MtRouterOpComponentVO1 implements Serializable {
    private static final long serialVersionUID = 1878085470810202643L;
    private String routerStepId;
    private String bomComponentId;

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public MtRouterOpComponentVO1() {}

    public MtRouterOpComponentVO1(String routerStepId, String bomComponentId) {
        this.routerStepId = routerStepId;
        this.bomComponentId = bomComponentId;
    }
}
