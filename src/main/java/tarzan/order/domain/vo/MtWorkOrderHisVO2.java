package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/9/27 15:39
 * @Description:
 */
public class MtWorkOrderHisVO2 implements Serializable {

    private static final long serialVersionUID = 7478183937993852866L;

    @ApiModelProperty(value = "生产指令历史ID")
    private String workOrderHisId;

    @ApiModelProperty(value = "事件ID")
    private String eventId;

    public String getWorkOrderHisId() {
        return workOrderHisId;
    }

    public void setWorkOrderHisId(String workOrderHisId) {
        this.workOrderHisId = workOrderHisId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
