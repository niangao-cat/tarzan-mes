package tarzan.order.domain.vo;

import java.io.Serializable;

/**
 * Created by slj on 2019-01-08.
 */
public class MtEoVO3 implements Serializable {

    private static final long serialVersionUID = -2943367920220350647L;

    private String eoId;
    private String eventId;
    private String status;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

}
