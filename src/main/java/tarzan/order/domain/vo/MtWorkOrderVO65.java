package tarzan.order.domain.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/30 13:50
 * @Description:
 */
public class MtWorkOrderVO65 implements Serializable {
    private static final long serialVersionUID = -7162995539477616682L;

    private List<MtWorkOrderVO29> woCompleteInfoList;

    private String workcellId;
    private String eventRequestId;
    private LocalDate shiftDate;
    private String shiftCode;

    public List<MtWorkOrderVO29> getWoCompleteInfoList() {
        return woCompleteInfoList;
    }

    public void setWoCompleteInfoList(List<MtWorkOrderVO29> woCompleteInfoList) {
        this.woCompleteInfoList = woCompleteInfoList;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
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
