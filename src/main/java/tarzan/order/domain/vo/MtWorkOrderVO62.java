package tarzan.order.domain.vo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author : MrZ
 * @date : 2020-08-25 12:49
 **/
public class MtWorkOrderVO62 extends MtWorkOrderVO61 implements Serializable {
    private static final long serialVersionUID = -8655873972755833304L;

    private String eventRequestId;
    private String shiftCode;
    private LocalDate shiftDate;

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
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
}
