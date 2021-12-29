package tarzan.calendar.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtCalendarShiftDTO2 implements Serializable {
    private static final long serialVersionUID = -8157314929588306744L;

    @ApiModelProperty("班次日期")
    private String shiftDate;
    @ApiModelProperty("班次列表")
    private List<MtCalendarShiftDTO> calendarShiftList;

    public String getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(String shiftDate) {
        this.shiftDate = shiftDate;
    }

    public List<MtCalendarShiftDTO> getCalendarShiftList() {
        return calendarShiftList;
    }

    public void setCalendarShiftList(List<MtCalendarShiftDTO> calendarShiftList) {
        this.calendarShiftList = calendarShiftList;
    }
}
