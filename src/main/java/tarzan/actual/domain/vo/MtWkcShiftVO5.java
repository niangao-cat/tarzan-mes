package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by slj on 2019-02-11.
 */
public class MtWkcShiftVO5 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3449071989742358987L;
    private String workcellId;
    private Date shiftStartTimeFrom;
    private Date shiftStartTimeTo;
    private Date shiftEndTimeFrom;
    private Date shiftEndTimeTo;

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public Date getShiftStartTimeFrom() {
        if (shiftStartTimeFrom == null) {
            return null;
        }
        return (Date) shiftStartTimeFrom.clone();
    }

    public void setShiftStartTimeFrom(Date shiftStartTimeFrom) {
        if (shiftStartTimeFrom == null) {
            this.shiftStartTimeFrom = null;
        } else {
            this.shiftStartTimeFrom = (Date) shiftStartTimeFrom.clone();
        }
    }

    public Date getShiftStartTimeTo() {
        if (shiftStartTimeTo == null) {
            return null;
        }
        return (Date) shiftStartTimeTo.clone();
    }

    public void setShiftStartTimeTo(Date shiftStartTimeTo) {
        if (shiftStartTimeTo == null) {
            this.shiftStartTimeTo = null;
        } else {
            this.shiftStartTimeTo = (Date) shiftStartTimeTo.clone();
        }
    }

    public Date getShiftEndTimeFrom() {
        if (shiftEndTimeFrom == null) {
            return null;
        }
        return (Date) shiftEndTimeFrom.clone();
    }

    public void setShiftEndTimeFrom(Date shiftEndTimeFrom) {
        if (shiftEndTimeFrom == null) {
            this.shiftEndTimeFrom = null;
        } else {
            this.shiftEndTimeFrom = (Date) shiftEndTimeFrom.clone();
        }
    }

    public Date getShiftEndTimeTo() {
        if (shiftEndTimeTo == null) {
            return null;
        }
        return (Date) shiftEndTimeTo.clone();
    }

    public void setShiftEndTimeTo(Date shiftEndTimeTo) {
        if (shiftEndTimeTo == null) {
            this.shiftEndTimeTo = null;
        } else {
            this.shiftEndTimeTo = (Date) shiftEndTimeTo.clone();
        }
    }


}
