package tarzan.calendar.api.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author benjamin
 */
public class MtCalendarShiftDTO4 implements Serializable {
    private static final long serialVersionUID = -596298993888616866L;

    @ApiModelProperty("作为班次日历分配唯一标识，用于其他数据结构引用工作日历")
    private String calendarShiftId;

    @ApiModelProperty(value = "日历ID")
    private String calendarId;

    @ApiModelProperty(value = "日历编码")
    private String calendarCode;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "班次所在日期，年月日")
    private Date shiftDate;

    @ApiModelProperty(value = "班次代码")
    private String shiftCode;

    @ApiModelProperty(value = "该班次是否有效")
    private String enableFlag;

    @ApiModelProperty(value = "班次开始时间")
    private Date shiftStartTime;

    @ApiModelProperty(value = "班次结束时间")
    private Date shiftEndTime;

    @ApiModelProperty(value = "班次内休息时间 小时")
    private Double restTime;

    @ApiModelProperty(value = "开动率 %")
    private Double utilizationRate;

    @ApiModelProperty(value = "借用能力 小时")
    private Double borrowingAbility;

    @ApiModelProperty(value = "能力单位")
    private String capacityUnit;

    @ApiModelProperty(value = "标准产量")
    private Double standardCapacity;

    @ApiModelProperty(value = "班次的顺序")
    private Long sequence;

    @ApiModelProperty(value = "周次")
    private Integer weekOfYear;

    @ApiModelProperty(value = "星期")
    private Integer dayOfWeek;

    public String getCalendarShiftId() {
        return calendarShiftId;
    }

    public void setCalendarShiftId(String calendarShiftId) {
        this.calendarShiftId = calendarShiftId;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getCalendarCode() {
        return calendarCode;
    }

    public void setCalendarCode(String calendarCode) {
        this.calendarCode = calendarCode;
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

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public Date getShiftStartTime() {
        if (shiftStartTime == null) {
            return null;
        } else {
            return (Date) shiftStartTime.clone();
        }
    }

    public void setShiftStartTime(Date shiftStartTime) {
        if (shiftStartTime == null) {
            this.shiftStartTime = null;
        } else {
            this.shiftStartTime = (Date) shiftStartTime.clone();
        }
    }

    public Date getShiftEndTime() {
        if (shiftEndTime == null) {
            return null;
        } else {
            return (Date) shiftEndTime.clone();
        }
    }

    public void setShiftEndTime(Date shiftEndTime) {
        if (shiftEndTime == null) {
            this.shiftEndTime = null;
        } else {
            this.shiftEndTime = (Date) shiftEndTime.clone();
        }
    }

    public Double getRestTime() {
        return restTime;
    }

    public void setRestTime(Double restTime) {
        this.restTime = restTime;
    }

    public Double getUtilizationRate() {
        return utilizationRate;
    }

    public void setUtilizationRate(Double utilizationRate) {
        this.utilizationRate = utilizationRate;
    }

    public Double getBorrowingAbility() {
        return borrowingAbility;
    }

    public void setBorrowingAbility(Double borrowingAbility) {
        this.borrowingAbility = borrowingAbility;
    }

    public String getCapacityUnit() {
        return capacityUnit;
    }

    public void setCapacityUnit(String capacityUnit) {
        this.capacityUnit = capacityUnit;
    }

    public Double getStandardCapacity() {
        return standardCapacity;
    }

    public void setStandardCapacity(Double standardCapacity) {
        this.standardCapacity = standardCapacity;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public Integer getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(Integer weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
