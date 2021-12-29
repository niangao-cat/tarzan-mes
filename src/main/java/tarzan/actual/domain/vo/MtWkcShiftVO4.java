package tarzan.actual.domain.vo;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by slj on 2019-02-11.
 */
public class MtWkcShiftVO4 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1584912447292217636L;
    private Date shiftTimeFrom;
    private Date shiftTimeTo;
    private String wkcShiftId;

    public String getWkcShiftId() {
        return wkcShiftId;
    }

    public void setWkcShiftId(String wkcShiftId) {
        this.wkcShiftId = wkcShiftId;
    }

    public Date getShiftTimeFrom() {
        if (shiftTimeFrom == null) {
            return null;
        }
        return (Date) shiftTimeFrom.clone();
    }

    public void setShiftTimeFrom(Date shiftTimeFrom) {
        if (shiftTimeFrom == null) {
            this.shiftTimeFrom = null;
        } else {
            this.shiftTimeFrom = (Date) shiftTimeFrom.clone();
        }
    }

    public Date getShiftTimeTo() {
        if (shiftTimeTo == null) {
            return null;
        }
        return (Date) shiftTimeTo.clone();
    }

    public void setShiftTimeTo(Date shiftTimeTo) {
        if (shiftTimeTo == null) {
            this.shiftTimeTo = null;
        } else {
            this.shiftTimeTo = (Date) shiftTimeTo.clone();
        }
    }
}
