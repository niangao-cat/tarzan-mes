package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import tarzan.method.domain.vo.MtBomComponentVO9;

/**
 * @author Leeloing
 * @date 2019/3/21 11:39
 */
public class MtEoComponentActualVO21 implements Serializable {

    private static final long serialVersionUID = 8703048138038045165L;

    private String eoId;
    private List<MtBomComponentVO9> bomComponentVO9s;
    private String workcellId;
    private String locatorId;
    private String parentEventId;
    private String eventRequestId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    private String shiftCode;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public List<MtBomComponentVO9> getBomComponentVO9s() {
        return bomComponentVO9s;
    }

    public void setBomComponentVO9s(List<MtBomComponentVO9> bomComponentVO9s) {
        this.bomComponentVO9s = bomComponentVO9s;
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
