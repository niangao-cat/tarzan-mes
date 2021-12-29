package tarzan.general.api.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class MtEventObjectTypeRelDTO implements Serializable {
    private static final long serialVersionUID = -14433878755935807L;

    public String getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(String eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public String getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(String objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    @ApiModelProperty("关系ID")
    @Id
    private String relId;
    @NotNull
    @ApiModelProperty(value = "事件类型",required = true)
    private String eventTypeId;
    @NotNull
    @ApiModelProperty(value = "对象类型ID",required = true)
    private String objectTypeId;
    @ApiModelProperty(value = "是否启用",required = true)
    private String enableFlag;
}
