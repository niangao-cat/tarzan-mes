package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by HP on 2019/3/19.
 */
public class MtWorkOrderVO13 implements Serializable {

    private static final long serialVersionUID = -3740026829933971817L;

    private String sourceWorkOrderId;
    private Double splitQty;
    private String outsideNum;
    private List<String> incomingValueList;
    private String workcellId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    private String shiftCode;
    private String locatorId;
    private String parentEventId;
    private String eventRequestId;

    public String getSourceWorkOrderId() {
        return sourceWorkOrderId;
    }

    public void setSourceWorkOrderId(String sourceWorkOrderId) {
        this.sourceWorkOrderId = sourceWorkOrderId;
    }

    public Double getSplitQty() {
        return splitQty;
    }

    public void setSplitQty(Double splitQty) {
        this.splitQty = splitQty;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
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

    public String getOutsideNum() {
        return outsideNum;
    }

    public void setOutsideNum(String outsideNum) {
        this.outsideNum = outsideNum;
    }

    public List<String> getIncomingValueList() {
        return incomingValueList;
    }

    public void setIncomingValueList(List<String> incomingValueList) {
        this.incomingValueList = incomingValueList;
    }
}
