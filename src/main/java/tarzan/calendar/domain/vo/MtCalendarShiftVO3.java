package tarzan.calendar.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MtCalendarShiftVO3 implements Serializable {

    private static final long serialVersionUID = -639390598640360116L;

    private String workcellId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate; // 班次所在日期，年月日
    private String shiftCode;
    private Integer forwardPeriod;
    private Integer backwardPeriod;
    private String calendarType;

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public Date getShiftDate() {
        if (shiftDate == null) {
            return null;
        }
        return (Date) shiftDate.clone();
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

    public Integer getForwardPeriod() {
        return forwardPeriod;
    }

    public void setForwardPeriod(Integer forwardPeriod) {
        this.forwardPeriod = forwardPeriod;
    }

    public Integer getBackwardPeriod() {
        return backwardPeriod;
    }

    public void setBackwardPeriod(Integer backwardPeriod) {
        this.backwardPeriod = backwardPeriod;
    }

    public String getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(String calendarType) {
        this.calendarType = calendarType;
    }
}
