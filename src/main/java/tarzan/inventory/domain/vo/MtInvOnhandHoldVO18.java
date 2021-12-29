package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @author chuang.yang
 * @date 2021/4/22
 */
public class MtInvOnhandHoldVO18 implements Serializable {

    private static final long serialVersionUID = 7367340011981140292L;
    private List<MtInvOnhandHoldVO> onhandReserveList;
    private String eventId;
    private String eventRequestId;
    private Date shiftDate;
    private String shiftCode;
    private String eventLocatorId;
    private String workcellId;

    public List<MtInvOnhandHoldVO> getOnhandReserveList() {
        return onhandReserveList;
    }

    public void setOnhandReserveList(List<MtInvOnhandHoldVO> onhandReserveList) {
        this.onhandReserveList = onhandReserveList;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public Date getShiftDate() {
        if (shiftDate != null) {
            return (Date) shiftDate.clone();
        } else {
            return null;
        }
    }

    public void setShiftDate(Date shiftDate) {
        if (shiftDate == null) {
            this.shiftDate = null;
        } else {
            this.shiftDate = (Date) shiftDate.clone();
        }
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public String getEventLocatorId() {
        return eventLocatorId;
    }

    public void setEventLocatorId(String eventLocatorId) {
        this.eventLocatorId = eventLocatorId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

}

