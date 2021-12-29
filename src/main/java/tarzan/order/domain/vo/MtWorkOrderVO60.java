package tarzan.order.domain.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author : MrZ
 * @date : 2020-08-25 15:30
 **/
public class MtWorkOrderVO60 implements Serializable {
    private static final long serialVersionUID = 819144804780910368L;
    private String eventRequestId;
    private String locatorId;
    private String shiftCode;
    private LocalDate shiftDate;
    private String workcellId;
    private List<MtWorkOrderVO61> woMessageList;

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public LocalDate getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDate shiftDate) {
        this.shiftDate = shiftDate;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public List<MtWorkOrderVO61> getWoMessageList() {
        return woMessageList;
    }

    public void setWoMessageList(List<MtWorkOrderVO61> woMessageList) {
        this.woMessageList = woMessageList;
    }

}
