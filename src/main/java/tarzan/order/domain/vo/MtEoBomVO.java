package tarzan.order.domain.vo;

import java.io.Serializable;

public class MtEoBomVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1921458956611377450L;
    private String eoId;
    private String bomId;
    private String eventId;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }



}

