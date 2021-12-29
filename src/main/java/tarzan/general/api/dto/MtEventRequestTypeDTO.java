package tarzan.general.api.dto;

import java.io.Serializable;
import io.swagger.annotations.ApiModelProperty;

public class MtEventRequestTypeDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4122906167404347704L;

    @ApiModelProperty(value = "事件请求编码")
    private String requestTypeCode;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "启用状态")
    private String enableFlag;

    public String getRequestTypeCode() {
        return requestTypeCode;
    }

    public void setRequestTypeCode(String requestTypeCode) {
        this.requestTypeCode = requestTypeCode;
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
