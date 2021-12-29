package tarzan.calendar.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MtCalendarShiftVO4 implements Serializable {
    private static final long serialVersionUID = -3196514226293997906L;

    private String calendarShiftId; // 主键
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate; // 班次所在日期，年月日
    private String shiftCode; // 班次代码

    public String getCalendarShiftId() {
        return calendarShiftId;
    }

    public void setCalendarShiftId(String calendarShiftId) {
        this.calendarShiftId = calendarShiftId;
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
}
