package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Leeloing
 * @date 2019/3/19 14:53
 */
public class MtEoComponentActualVO18 implements Serializable {
    private static final long serialVersionUID = -1223988544960863572L;


    private String primaryEoId;
    private List<String> secondaryEoIds;
    private String targetEoId;
    private String workcellId;
    private String locatorId;
    private String parentEventId;
    private String eventRequestId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    private String shiftCode;

    public String getPrimaryEoId() {
        return primaryEoId;
    }

    public void setPrimaryEoId(String primaryEoId) {
        this.primaryEoId = primaryEoId;
    }

    public List<String> getSecondaryEoIds() {
        return secondaryEoIds;
    }

    public void setSecondaryEoIds(List<String> secondaryEoIds) {
        this.secondaryEoIds = secondaryEoIds;
    }

    public String getTargetEoId() {
        return targetEoId;
    }

    public void setTargetEoId(String targetEoId) {
        this.targetEoId = targetEoId;
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

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }
}
