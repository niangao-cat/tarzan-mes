package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepWorkcellActualVO1 implements Serializable {
    private static final long serialVersionUID = -6020074421191280828L;

    private String eoStepActualId; // EO步骤实绩主键
    private String workcellId; // EO在此步骤的工作单元
    private Double queueQty; // 排队数量
    private Double workingQty; // 正在加工的数量
    private Double completePendingQty; // 完成暂存数量
    private Double completedQty; // 完成的数量
    private Double scrappedQty; // 报废的数量
    private String eventId; // 事件id

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

    public Double getQueueQty() {
        return queueQty;
    }

    public void setQueueQty(Double queueQty) {
        this.queueQty = queueQty;
    }

    public Double getWorkingQty() {
        return workingQty;
    }

    public void setWorkingQty(Double workingQty) {
        this.workingQty = workingQty;
    }

    public Double getCompletePendingQty() {
        return completePendingQty;
    }

    public void setCompletePendingQty(Double completePendingQty) {
        this.completePendingQty = completePendingQty;
    }

    public Double getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(Double completedQty) {
        this.completedQty = completedQty;
    }

    public Double getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
