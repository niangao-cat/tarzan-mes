package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtEoRouterActualVO22 implements Serializable {
    private static final long serialVersionUID = 9126530497408926946L;

    private String primarySourceEoId; // 主来源执行作业唯一标识
    private List<String> secondSourceEoIds; // 副来源执行作业
    private String targetEoId; // 拆分目标执行作业
    private String eventRequestId; //
    private String parenEventId; //

    public String getPrimarySourceEoId() {
        return primarySourceEoId;
    }

    public void setPrimarySourceEoId(String primarySourceEoId) {
        this.primarySourceEoId = primarySourceEoId;
    }

    public List<String> getSecondSourceEoIds() {
        return secondSourceEoIds;
    }

    public void setSecondSourceEoIds(List<String> secondSourceEoIds) {
        this.secondSourceEoIds = secondSourceEoIds;
    }

    public String getTargetEoId() {
        return targetEoId;
    }

    public void setTargetEoId(String targetEoId) {
        this.targetEoId = targetEoId;
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
