package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepWipVO6 implements Serializable {
    private static final long serialVersionUID = 3151134599657028347L;

    private String eoId; // 执行作业唯一标识
    private String eoStepActualId; // 步骤实绩
    private String routerId; // 工艺路线
    private String routerStepId; // 步骤ID
    private String status; // 步骤状态

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
