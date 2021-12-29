package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Leeloing
 * @date 2019/3/21 14:31
 */
public class MtEoVO25 implements Serializable {

    private static final long serialVersionUID = 4689162636232504351L;

    private String sourceEoId;
    private String coproductMaterialId;
    private Double qty;
    private String workcellId;
    private String locatorId;
    private String parentEventId;
    private String eventRequestId;
    private String shiftCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;

    public String getSourceEoId() {
        return sourceEoId;
    }

    public void setSourceEoId(String sourceEoId) {
        this.sourceEoId = sourceEoId;
    }

    public String getCoproductMaterialId() {
        return coproductMaterialId;
    }

    public void setCoproductMaterialId(String coproductMaterialId) {
        this.coproductMaterialId = coproductMaterialId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
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
