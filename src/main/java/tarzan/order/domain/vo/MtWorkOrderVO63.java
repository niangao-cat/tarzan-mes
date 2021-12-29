package tarzan.order.domain.vo;

import java.io.Serializable;
import java.time.LocalDate;

public class MtWorkOrderVO63 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3300759562164206234L;
    private String makeOrderNum;
    private String workcellId;
    private String locatorId;
    private Long eventRequestId;
    private LocalDate shiftDate;
    private String shiftCode;

    public String getMakeOrderNum() {
        return makeOrderNum;
    }

    public void setMakeOrderNum(String makeOrderNum) {
        this.makeOrderNum = makeOrderNum;
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

    public Long getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(Long eventRequestId) {
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
