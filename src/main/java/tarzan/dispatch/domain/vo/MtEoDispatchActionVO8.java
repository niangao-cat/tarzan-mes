package tarzan.dispatch.domain.vo;

import java.io.Serializable;

public class MtEoDispatchActionVO8 implements Serializable {
    private static final long serialVersionUID = 5649823690150864400L;

    private String routerStepId; // 执行作业步骤ID
    private String eoDispatchProcessId; // 调度过程主键ID
    private Double assignQty; // 分配数量
    private String eoId;

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getEoDispatchProcessId() {
        return eoDispatchProcessId;
    }

    public void setEoDispatchProcessId(String eoDispatchProcessId) {
        this.eoDispatchProcessId = eoDispatchProcessId;
    }

    public Double getAssignQty() {
        return assignQty;
    }

    public void setAssignQty(Double assignQty) {
        this.assignQty = assignQty;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

}
