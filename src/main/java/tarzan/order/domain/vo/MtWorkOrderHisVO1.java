package tarzan.order.domain.vo;

import tarzan.order.domain.entity.MtWorkOrderHis;

public class MtWorkOrderHisVO1 extends MtWorkOrderHis {

    private static final long serialVersionUID = 7721512274645521377L;
    private String eventType;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

}
