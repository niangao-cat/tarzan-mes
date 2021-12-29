package tarzan.order.domain.vo;

import java.io.Serializable;

public class MtEoVO16 implements Serializable {
    private static final long serialVersionUID = -4946286848201954065L;

    private String eoId;
    private String eventId;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
