package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtEoRouterActualVO33
 * @description
 * @date 2019年11月25日 14:10
 */
public class MtEoRouterActualVO33 implements Serializable {
    private static final long serialVersionUID = 580989320484289780L;

    @ApiModelProperty(value = "目标执行作业唯一标识")
    private List<MtEoRouterActualVO34> eoMessageList;

    @ApiModelProperty(value = "来源执行作业ID")
    private String sourceEoId;

    @ApiModelProperty(value = "事件ID")
    private String eventId;

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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
