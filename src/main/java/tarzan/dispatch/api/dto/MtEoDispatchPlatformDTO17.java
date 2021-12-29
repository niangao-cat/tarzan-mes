package tarzan.dispatch.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author : MrZ
 * @date : 2019-12-10 19:23
 **/
public class MtEoDispatchPlatformDTO17 implements Serializable {
    private static final long serialVersionUID = -7396385918212569303L;
    @ApiModelProperty(value = "主键ID", required = true)
    private String eoDispatchId;
    @ApiModelProperty("调度状态(DISPATCH_STATUS),PUBLISH或者UNPUBLISH")
    private String eoDispatchStatus;
    @ApiModelProperty(value = "顺序", required = true)
    private Long sequence;

    public String getEoDispatchStatus() {
        return eoDispatchStatus;
    }

    public void setEoDispatchStatus(String eoDispatchStatus) {
        this.eoDispatchStatus = eoDispatchStatus;
    }

    public String getEoDispatchId() {
        return eoDispatchId;
    }

    public void setEoDispatchId(String eoDispatchId) {
        this.eoDispatchId = eoDispatchId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }
}
