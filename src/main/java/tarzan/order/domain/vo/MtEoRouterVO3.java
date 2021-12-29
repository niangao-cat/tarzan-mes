package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-11-21 15:55
 */
public class MtEoRouterVO3 implements Serializable {
    private static final long serialVersionUID = -4658686942680302497L;
    @ApiModelProperty("执行作业信息列表")
    private List<MtEoRouterVO1> eoMessageList = new ArrayList<>();
    @ApiModelProperty("事件ID")
    private String eventId;

    public List<MtEoRouterVO1> getEoMessageList() {
        return eoMessageList;
    }

    public void setEoMessageList(List<MtEoRouterVO1> eoMessageList) {
        this.eoMessageList = eoMessageList;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
