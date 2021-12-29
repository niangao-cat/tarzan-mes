package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoRouterActualVO13 implements Serializable {
    private static final long serialVersionUID = 2674356990357501010L;

    private String eoId; // 执行作业工艺路线实绩唯一标识
    private String workcellId; // 工作单元唯一标识
    private Double qty; // 数量
    private String eventRequestId; // 事件组ID
    private String routerStepId; // 工艺路线步骤唯一标识
    private String sourceEoStepActualId; // 来源步骤

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
    }
}
