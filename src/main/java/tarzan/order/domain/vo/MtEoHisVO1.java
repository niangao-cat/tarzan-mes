package tarzan.order.domain.vo;


import tarzan.order.domain.entity.MtEoHis;

public class MtEoHisVO1 extends MtEoHis {

    private static final long serialVersionUID = 890159407611183826L;
    private String eventType;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

}
