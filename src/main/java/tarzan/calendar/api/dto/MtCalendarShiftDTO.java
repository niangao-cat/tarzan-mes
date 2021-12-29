package tarzan.calendar.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtCalendarShiftDTO implements Serializable {
    private static final long serialVersionUID = 1292196520121085700L;

    @ApiModelProperty("班次编码")
    private String shiftCode;
    @ApiModelProperty("班次开始时间")
    private String shiftStartTime;
    @ApiModelProperty("班次结束时间")
    private String shiftEndTime;
    @ApiModelProperty("班次时间周期")
    private String shiftTimePeriod;

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public String getShiftStartTime() {
        return shiftStartTime;
    }

    public void setShiftStartTime(String shiftStartTime) {
        this.shiftStartTime = shiftStartTime;
    }

    public String getShiftEndTime() {
        return shiftEndTime;
    }

    public void setShiftEndTime(String shiftEndTime) {
        this.shiftEndTime = shiftEndTime;
    }

    public String getShiftTimePeriod() {
        return shiftTimePeriod;
    }

    public void setShiftTimePeriod(String shiftTimePeriod) {
        this.shiftTimePeriod = shiftTimePeriod;
    }
}
