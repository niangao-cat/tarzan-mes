package tarzan.order.domain.vo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MtWorkOrderHisVO implements Serializable {

    private static final long serialVersionUID = 5901545541970548906L;
    private String workOrderId;
    private String eventId;
    private String eventTimeFrom;
    private String eventTimeTo;
    private String eventBy;

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

    public String getEventTimeFrom() {
        return eventTimeFrom;
    }

    public void setEventTimeFrom(String eventTimeFrom) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(eventTimeFrom);
            this.eventTimeFrom = format.format(date);
        } catch (ParseException e) {
            this.eventTimeFrom = null;
        }
    }

    public String getEventTimeTo() {
        return eventTimeTo;
    }

    public void setEventTimeTo(String eventTimeTo) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(eventTimeTo);
            this.eventTimeTo = format.format(date);
        } catch (ParseException e) {
            this.eventTimeTo = null;
        }
    }

    public String getEventBy() {
        return eventBy;
    }

    public void setEventBy(String eventBy) {
        this.eventBy = eventBy;
    }

}
