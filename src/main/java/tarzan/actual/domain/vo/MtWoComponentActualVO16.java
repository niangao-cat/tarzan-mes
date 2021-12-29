package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import tarzan.method.domain.vo.MtBomComponentVO9;

/**
 * Created by HP on 2019/3/20.
 */
public class MtWoComponentActualVO16 implements Serializable {

    private static final long serialVersionUID = -5742644609171301797L;

    private String workOrderId;
    private List<MtBomComponentVO9> lines = new ArrayList<>();
    private String workcellId;
    private String locatorId;
    private String parentEventId;
    private String eventRequestId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    private String shiftCode;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public List<MtBomComponentVO9> getLines() {
        return lines;
    }

    public void setLines(List<MtBomComponentVO9> lines) {
        this.lines = lines;
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
