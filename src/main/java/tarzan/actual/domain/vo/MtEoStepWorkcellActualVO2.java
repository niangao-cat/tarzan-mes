package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepWorkcellActualVO2 implements Serializable {

    private static final long serialVersionUID = -2718812593794836930L;

    private String eoStepActualId; // EO步骤实绩主键
    private String workcellId; // EO在此步骤的工作单元
    private String eventId; // 事件id
    private Double queueQty; // 排队数量

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Double getQueueQty() {
        return queueQty;
    }

    public void setQueueQty(Double queueQty) {
        this.queueQty = queueQty;
    }
}
