package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoRouterActualVO25 implements Serializable {
    private static final long serialVersionUID = -30796582116748215L;

    private String targetEoId; // 目标执行作业唯一标识
    private String sourceEoStepActualId; // 来源步骤实绩
    private String eventId; //
    private Double qty; // 数量

    public String getTargetEoId() {
        return targetEoId;
    }

    public void setTargetEoId(String targetEoId) {
        this.targetEoId = targetEoId;
    }

    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }
}
