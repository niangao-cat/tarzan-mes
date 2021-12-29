package tarzan.calendar.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/10 14:41
 * @Author: ${yiyang.xie}
 */
public class MtCalendarShiftVO10 implements Serializable {
    private static final long serialVersionUID = 2572001170964718968L;

    @ApiModelProperty("日历班次ID")
    private String calendarShiftId;
    @ApiModelProperty("日历ID")
    private String calendarId;
    @ApiModelProperty("日历班次日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date calendarShiftDate;
    @ApiModelProperty("日历班次编码")
    private String calendarShiftCode;
    @ApiModelProperty("是否有效")
    private String enableFlag;
    @ApiModelProperty("班次开始时间")
    private Date shiftStartTime;
    @ApiModelProperty("班次结束时间")
    private Date shiftEndTime;
    @ApiModelProperty("休息时间（小时）")
    private Double restTime;
    @ApiModelProperty("开动率（%）")
    private Double utilizationRate;
    @ApiModelProperty("借用能力（小时）")
    private Double borrowingAbility;
    @ApiModelProperty("能力单位")
    private String capacityUnit;
    @ApiModelProperty("标准产量")
    private Double standardCapacity;
    @ApiModelProperty("顺序")
    private Long sequence;

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

    public Date getCalendarShiftDate() {
        if (calendarShiftDate != null) {
            return (Date) calendarShiftDate.clone();
        } else {
            return null;
        }
    }

    public void setCalendarShiftDate(Date calendarShiftDate) {
        if (calendarShiftDate == null) {
            this.calendarShiftDate = null;
        } else {
            this.calendarShiftDate = (Date) calendarShiftDate.clone();
        }
    }

    public String getCalendarShiftCode() {
        return calendarShiftCode;
    }

    public void setCalendarShiftCode(String calendarShiftCode) {
        this.calendarShiftCode = calendarShiftCode;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public Date getShiftStartTime() {
        if (shiftStartTime != null) {
            return (Date) shiftStartTime.clone();
        } else {
            return null;
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
        if (shiftEndTime != null) {
            return (Date) shiftEndTime.clone();
        } else {
            return null;
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
}
