package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Leeloing
 * @date 2019/3/22 9:12
 */
public class MtAssembleConfirmActualVO implements Serializable {

    private static final long serialVersionUID = -7622170028690937965L;

    private String assembleConfirmActualId;
    private String confirmBy;
    private String workcellId;
    private String locatorId;
    private String parentEventId;
    private String eventRequestId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    private String shiftCode;

    public String getAssembleConfirmActualId() {
        return assembleConfirmActualId;
    }

    public void setAssembleConfirmActualId(String assembleConfirmActualId) {
        this.assembleConfirmActualId = assembleConfirmActualId;
    }

    public String getConfirmBy() {
        return confirmBy;
    }

    public void setConfirmBy(String confirmBy) {
        this.confirmBy = confirmBy;
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

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public Date getShiftDate() {
        if (shiftDate == null) {
            return null;
        } else {
            return (Date) shiftDate.clone();
        }
    }

    public void setShiftDate(Date shiftDate) {

        if (shiftDate == null) {
            this.shiftDate = null;
        } else {
            this.shiftDate = (Date) shiftDate.clone();
        }
    }
}
