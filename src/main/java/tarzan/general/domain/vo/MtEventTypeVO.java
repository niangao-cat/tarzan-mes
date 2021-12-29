package tarzan.general.domain.vo;

import java.io.Serializable;

public class MtEventTypeVO implements Serializable {

    private static final long serialVersionUID = 2009377279014218809L;
    private String eventTypeCode;
    private String description;
    private String enableFlag;
    private String defaultEventTypeFlag;
    private String onhandChangeFlag;
    private String onhandChangeType;

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getDefaultEventTypeFlag() {
        return defaultEventTypeFlag;
    }

    public void setDefaultEventTypeFlag(String defaultEventTypeFlag) {
        this.defaultEventTypeFlag = defaultEventTypeFlag;
    }

    public String getOnhandChangeFlag() {
        return onhandChangeFlag;
    }

    public void setOnhandChangeFlag(String onhandChangeFlag) {
        this.onhandChangeFlag = onhandChangeFlag;
    }

    public String getOnhandChangeType() {
        return onhandChangeType;
    }

    public void setOnhandChangeType(String onhandChangeType) {
        this.onhandChangeType = onhandChangeType;
    }
}
