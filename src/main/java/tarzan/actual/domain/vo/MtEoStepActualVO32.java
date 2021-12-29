package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-11-27 18:03
 */
public class MtEoStepActualVO32 implements Serializable {
    private static final long serialVersionUID = 6330100958175620489L;
    @ApiModelProperty(value = "目标执行作业唯一标识")
    private List<MtEoRouterActualVO34> eoMessageList;

    @ApiModelProperty(value = "来源执行作业ID")
    private String sourceEoId;

    @ApiModelProperty(value = "事件请求ID")
    private String eventRequestId;

    @ApiModelProperty(value = "父事件ID")
    private String parenEventId;

    public List<MtEoRouterActualVO34> getEoMessageList() {
        return eoMessageList;
    }

    public void setEoMessageList(List<MtEoRouterActualVO34> eoMessageList) {
        this.eoMessageList = eoMessageList;
    }

    public String getSourceEoId() {
        return sourceEoId;
    }

    public void setSourceEoId(String sourceEoId) {
        this.sourceEoId = sourceEoId;
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
