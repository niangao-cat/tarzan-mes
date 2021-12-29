package tarzan.dispatch.domain.vo;

import java.io.Serializable;

public class MtEoDispatchActionVO2 implements Serializable {
    private static final long serialVersionUID = 3898055271608358749L;

    private String routerStepId; // 执行作业步骤实绩ID
    private String eoId;
    private String workcellId; // 工作单元ID

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

}
