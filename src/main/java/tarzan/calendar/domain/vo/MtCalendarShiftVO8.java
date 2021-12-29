package tarzan.calendar.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-09-27 09:51
 */
public class MtCalendarShiftVO8 implements Serializable {

    private static final long serialVersionUID = -584497807910308747L;
    @ApiModelProperty("班次所在日期，年月日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;

    @ApiModelProperty("下一班次编码")
    private String shiftCode;

    @ApiModelProperty("日历班次ID")
    private String calendarShiftId;

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

    public String getCalendarShiftId() {
        return calendarShiftId;
    }

    public void setCalendarShiftId(String calendarShiftId) {
        this.calendarShiftId = calendarShiftId;
    }
}
