package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtWkcShiftVO9
 * @description
 * @date 2019年10月10日 19:05
 */
public class MtWkcShiftVO9 implements Serializable {
    private static final long serialVersionUID = 4414769747695043686L;

    @ApiModelProperty("唯一标识")
    private String wkcShiftId;
    @ApiModelProperty("工作单元")
    private String workcellId;
    @ApiModelProperty("工作单元编码")
    private String workcellCode;
    @ApiModelProperty("工作单元短描述")
    private String workcellName;
    @ApiModelProperty("工作单元长描述")
    private String workcellDescription;
    @ApiModelProperty("班次日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    @ApiModelProperty("班次编码")
    private String shiftCode;
    @ApiModelProperty("班次开始时间")
    private Date shiftStartTime;
    @ApiModelProperty("班次结束时间")
    private Date shiftEndTime;

    public String getWkcShiftId() {
        return wkcShiftId;
    }

    public void setWkcShiftId(String wkcShiftId) {
        this.wkcShiftId = wkcShiftId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }


    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
    }

    public String getWorkcellName() {
        return workcellName;
    }

    public void setWorkcellName(String workcellName) {
        this.workcellName = workcellName;
    }

    public String getWorkcellDescription() {
        return workcellDescription;
    }

    public void setWorkcellDescription(String workcellDescription) {
        this.workcellDescription = workcellDescription;
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

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
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
}
