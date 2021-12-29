package tarzan.calendar.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/14 23:49
 * @Author: ${yiyang.xie}
 */
public class MtCalendarShiftVO12 implements Serializable {
    private static final long serialVersionUID = -3293705891544609307L;
    @ApiModelProperty("班次日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    @ApiModelProperty("班次编码")
    private String shiftCode;
    @ApiModelProperty("目标日历班次")
    private String targetCalendarShiftId;
    @ApiModelProperty("顺序")
    private Long sequence;

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

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public String getTargetCalendarShiftId() {
        return targetCalendarShiftId;
    }

    public void setTargetCalendarShiftId(String targetCalendarShiftId) {
        this.targetCalendarShiftId = targetCalendarShiftId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtCalendarShiftVO12 vo12 = (MtCalendarShiftVO12) o;
        return Objects.equals(shiftDate, vo12.shiftDate) && Objects.equals(shiftCode, vo12.shiftCode)
                        && Objects.equals(targetCalendarShiftId, vo12.targetCalendarShiftId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shiftDate, shiftCode, targetCalendarShiftId);
    }
}
