package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepActualVO26 implements Serializable {
    private static final long serialVersionUID = 6237878295358419515L;

    private String eoRouterActualId; // 执行作业工艺路线实绩唯一标识
    private String eoStepActualId; // 执行作业步骤唯一标识
    private String routerId; // 工艺路线唯一标识
    private String routerStepId;; // 工艺路线步骤唯一标识
    private String routerGroupStepId; // 步骤组步骤标识

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

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

    public String getRouterGroupStepId() {
        return routerGroupStepId;
    }

    public void setRouterGroupStepId(String routerGroupStepId) {
        this.routerGroupStepId = routerGroupStepId;
    }
}
