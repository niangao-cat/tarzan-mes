package tarzan.general.api.dto;

import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class MtEventTypeDTO2 implements Serializable  {
    private static final long serialVersionUID = -6801606355999656382L;

    @ApiModelProperty(value = "事件类型编码")
    private String eventTypeCode;
    @ApiModelProperty(value = "描述")
    @MultiLanguageField
    private String description;
    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

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
}
