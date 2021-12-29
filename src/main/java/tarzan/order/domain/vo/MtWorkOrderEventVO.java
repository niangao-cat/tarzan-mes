package tarzan.order.domain.vo;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2019/7/2 14:57
 * @Description:
 */
public class MtWorkOrderEventVO implements Serializable {
    private static final long serialVersionUID = 2794272595684490772L;

    private String workOrderId;
    private String eventId;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
