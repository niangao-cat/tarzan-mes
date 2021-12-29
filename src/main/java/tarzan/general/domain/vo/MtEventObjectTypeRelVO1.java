package tarzan.general.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class MtEventObjectTypeRelVO1 implements Serializable {
    private static final long serialVersionUID = -4582655350821307989L;

    @ApiModelProperty(value = "事件类型编码",required = true)
    private String eventTypeCode;
    @ApiModelProperty(value = "有效性")
    private String enableFlag;

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
