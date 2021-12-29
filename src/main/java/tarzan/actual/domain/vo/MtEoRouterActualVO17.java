package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtEoRouterActualVO17 implements Serializable {
    private static final long serialVersionUID = -5015826425503483837L;

    private String targetEoId; // 目标执行作业唯一标识
    private List<String> sourceEoStepActualIds; // 来源步骤实绩
    private String eventId; //
    private Double qty; // 数量

    public String getTargetEoId() {
        return targetEoId;
    }

    public void setTargetEoId(String targetEoId) {
        this.targetEoId = targetEoId;
    }

    public List<String> getSourceEoStepActualIds() {
        return sourceEoStepActualIds;
    }

    public void setSourceEoStepActualIds(List<String> sourceEoStepActualIds) {
        this.sourceEoStepActualIds = sourceEoStepActualIds;
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
