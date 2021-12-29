package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepActualVO9 implements Serializable {
    private static final long serialVersionUID = -8243557244752483564L;

    private String eoStepActualId; // 步骤实绩唯一标识
    private String eventId; // 事件
    private String bypassedFlag; // 更新标识
    private String updateType; // 更新类型

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getBypassedFlag() {
        return bypassedFlag;
    }

    public void setBypassedFlag(String bypassedFlag) {
        this.bypassedFlag = bypassedFlag;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }
}
