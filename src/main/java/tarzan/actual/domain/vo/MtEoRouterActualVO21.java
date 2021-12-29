package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoRouterActualVO21 implements Serializable {
    private static final long serialVersionUID = -4995457408441281731L;

    private String sourceEoId; // 来源执行作业唯一标识
    private String targetEoId; // 拆分目标执行作业
    private Double qty; // 拆分数量
    private String eventRequestId; //
    private String parenEventId; //

    public String getSourceEoId() {
        return sourceEoId;
    }

    public void setSourceEoId(String sourceEoId) {
        this.sourceEoId = sourceEoId;
    }

    public String getTargetEoId() {
        return targetEoId;
    }

    public void setTargetEoId(String targetEoId) {
        this.targetEoId = targetEoId;
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

    public String getParenEventId() {
        return parenEventId;
    }

    public void setParenEventId(String parenEventId) {
        this.parenEventId = parenEventId;
    }
}
