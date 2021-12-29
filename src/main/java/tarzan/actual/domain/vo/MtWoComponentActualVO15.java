package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by HP on 2019/3/19.
 */
public class MtWoComponentActualVO15 implements Serializable {

    private static final long serialVersionUID = 2551497867093927283L;

    private String primaryWorkOrderId;
    private List<String> secondaryWorkOrderIds;
    private String targetWorkOrderId;
    private String workcellId;
    private String locatorId;
    private String parentEventId;
    private String eventRequestId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    private String shiftCode;

    public String getPrimaryWorkOrderId() {
        return primaryWorkOrderId;
    }

    public void setPrimaryWorkOrderId(String primaryWorkOrderId) {
        this.primaryWorkOrderId = primaryWorkOrderId;
    }

    public List<String> getSecondaryWorkOrderIds() {
        return secondaryWorkOrderIds;
    }

    public void setSecondaryWorkOrderIds(List<String> secondaryWorkOrderIds) {
        this.secondaryWorkOrderIds = secondaryWorkOrderIds;
    }

    public String getTargetWorkOrderId() {
        return targetWorkOrderId;
    }

    public void setTargetWorkOrderId(String targetWorkOrderId) {
        this.targetWorkOrderId = targetWorkOrderId;
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
        return shiftDate == null ? null : (Date) shiftDate.clone();
    }

    public void setShiftDate(Date shiftDate) {
        this.shiftDate = shiftDate == null ? null : (Date) shiftDate.clone();
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }
}
