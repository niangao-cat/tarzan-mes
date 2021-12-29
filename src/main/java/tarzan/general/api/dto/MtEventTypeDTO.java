package tarzan.general.api.dto;

import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

public class MtEventTypeDTO implements Serializable {
    private static final long serialVersionUID = 7098916892910611536L;

    @ApiModelProperty("事件类型ID")
    @Id
    private String eventTypeId;
    @ApiModelProperty(value = "事件类型编码", required = true)
    @NotNull
    private String eventTypeCode;
    @ApiModelProperty(value = "描述")
    @MultiLanguageField
    private String description;
    @ApiModelProperty(value = "是否有效", required = true)
    private String enableFlag;
    @ApiModelProperty(value = "系统初始事件类型标识", required = true)

    private String defaultEventTypeFlag;
    @ApiModelProperty(value = "是否影响现有量标识", required = true)

    private String onhandChangeFlag;
    @ApiModelProperty(value = "现有量变化类型（增加/减少）")
    private String onhandChangeType;

    public String getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(String eventTypeId) {
        this.eventTypeId = eventTypeId;
    }
    private Map<String, Map<String, String>> _tls;

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }

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
