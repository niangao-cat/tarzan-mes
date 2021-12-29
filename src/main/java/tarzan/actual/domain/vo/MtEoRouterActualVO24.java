package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoRouterActualVO24 implements Serializable {
    private static final long serialVersionUID = 8044782952684412949L;

    private String currentRouterId; // 当前工艺路线
    private String routerStepId; // 当前步骤（完成或返回步骤）
    private String eoRouterActualId; // 当前工艺路线实绩
    private String eoRouterCompletedFlag; // 当前工艺路线完成标识

    public String getCurrentRouterId() {
        return currentRouterId;
    }

    public void setCurrentRouterId(String currentRouterId) {
        this.currentRouterId = currentRouterId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getEoRouterCompletedFlag() {
        return eoRouterCompletedFlag;
    }

    public void setEoRouterCompletedFlag(String eoRouterCompletedFlag) {
        this.eoRouterCompletedFlag = eoRouterCompletedFlag;
    }
}
