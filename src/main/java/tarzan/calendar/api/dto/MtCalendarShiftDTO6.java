package tarzan.calendar.api.dto;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtCalendarShiftDTO6 implements Serializable {
    private static final long serialVersionUID = 1051071240826506973L;

    @ApiModelProperty("来源日历Id")
    private String sourceCalendarId;

    @ApiModelProperty("来源日历类型")
    private String sourceCalendarType;

    @ApiModelProperty("目标日历Id")
    private String targetCalendarId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("来源日历班次开始日期")
    private LocalDate sourceShiftStartDate;

    @ApiModelProperty("来源日历班次结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sourceShiftEndDate;

    @ApiModelProperty("目标日历班次开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate targetShiftStartDate;

    @ApiModelProperty("目标日历班次开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate targetShiftEndDate;

    public String getSourceCalendarId() {
        return sourceCalendarId;
    }

    public void setSourceCalendarId(String sourceCalendarId) {
        this.sourceCalendarId = sourceCalendarId;
    }

    public String getSourceCalendarType() {
        return sourceCalendarType;
    }

    public void setSourceCalendarType(String sourceCalendarType) {
        this.sourceCalendarType = sourceCalendarType;
    }

    public String getTargetCalendarId() {
        return targetCalendarId;
    }

    public void setTargetCalendarId(String targetCalendarId) {
        this.targetCalendarId = targetCalendarId;
    }

    public LocalDate getSourceShiftStartDate() {
        return sourceShiftStartDate;
    }

    public void setSourceShiftStartDate(LocalDate sourceShiftStartDate) {
        this.sourceShiftStartDate = sourceShiftStartDate;
    }

    public LocalDate getSourceShiftEndDate() {
        return sourceShiftEndDate;
    }

    public void setSourceShiftEndDate(LocalDate sourceShiftEndDate) {
        this.sourceShiftEndDate = sourceShiftEndDate;
    }

    public LocalDate getTargetShiftStartDate() {
        return targetShiftStartDate;
    }

    public void setTargetShiftStartDate(LocalDate targetShiftStartDate) {
        this.targetShiftStartDate = targetShiftStartDate;
    }

    public LocalDate getTargetShiftEndDate() {
        return targetShiftEndDate;
    }

    public void setTargetShiftEndDate(LocalDate targetShiftEndDate) {
        this.targetShiftEndDate = targetShiftEndDate;
    }
}
