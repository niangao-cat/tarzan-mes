package tarzan.order.domain.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class MtEoVO50 implements Serializable {
    private static final long serialVersionUID = -688065468046185907L;

    private List<String> eoIds;
    private String workcellId;
    private String locatorId;
    private String parentEventId;
    private String eventRequestId;
    private LocalDate shiftDate;
    private String shiftCode;

    public List<String> getEoIds() {
        return eoIds;
    }

    public void setEoIds(List<String> eoIds) {
        this.eoIds = eoIds;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getParentEventId() {
        return parentEventId;
    }

    public void setParentEventId(String parentEventId) {
        this.parentEventId = parentEventId;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public LocalDate getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDate shiftDate) {
        this.shiftDate = shiftDate;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }
}
