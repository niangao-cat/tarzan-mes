package tarzan.order.domain.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @Author: chuang.yang
 * @Date: 2020/9/25 9:38
 * @Description:
 */
public class MtWorkOrderVO64 implements Serializable {
    private static final long serialVersionUID = 8081375479126134914L;

    private List<String> makeOrderNums;
    private String workcellId;
    private String locatorId;
    private String eventRequestId;
    private LocalDate shiftDate;
    private String shiftCode;

    public List<String> getMakeOrderNums() {
        return makeOrderNums;
    }

    public void setMakeOrderNums(List<String> makeOrderNums) {
        this.makeOrderNums = makeOrderNums;
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
