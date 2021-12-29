package tarzan.calendar.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * MtCalendarShiftVO14
 *
 * @author: {xieyiyang}
 * @date: 2020/2/11 17:31
 * @description:
 */
public class MtCalendarShiftVO14 implements Serializable {
    private static final long serialVersionUID = 797214646458110960L;

    @ApiModelProperty("日历班次ID")
    private String calendarShiftId;
    @ApiModelProperty("班次编码")
    private String shiftCode;
    @ApiModelProperty("班次日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;

    public String getCalendarShiftId() {
        return calendarShiftId;
    }

    public void setCalendarShiftId(String calendarShiftId) {
        this.calendarShiftId = calendarShiftId;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public Date getShiftDate() {
        if (shiftDate != null) {
            return (Date) shiftDate.clone();
        } else {
            return null;
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
