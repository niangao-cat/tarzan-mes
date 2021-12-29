package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/11/26 9:42
 * @Author: ${yiyang.xie}
 */
public class MtEoActualVO11 implements Serializable {
    private static final long serialVersionUID = 1104995962395661261L;

    @ApiModelProperty("eoActual列表")
    private List<MtEoActualVO10> eoMessageList;

    @ApiModelProperty("事件ID")
    private String eventId;

    public List<MtEoActualVO10> getEoMessageList() {
        return eoMessageList;
    }

    public void setEoMessageList(List<MtEoActualVO10> eoMessageList) {
        this.eoMessageList = eoMessageList;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
