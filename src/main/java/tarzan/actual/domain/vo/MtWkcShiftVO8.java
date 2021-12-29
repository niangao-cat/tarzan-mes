package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtWkcShiftVO8
 * @description
 * @date 2019年10月10日 19:03
 */
public class MtWkcShiftVO8 implements Serializable {
    private static final long serialVersionUID = -6216724761222850671L;

    @ApiModelProperty("唯一标识")
    private String wkcShiftId;
    @ApiModelProperty("工作单元")
    private String workcellId;
    @ApiModelProperty("班次日期从")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDateFrom;
    @ApiModelProperty("班次日期至")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDateTo;
    @ApiModelProperty("班次编码")
    private String shiftCode;
    @ApiModelProperty("班次开始时间从")
    private Date shiftStartTimeFrom;
    @ApiModelProperty("班次开始时间至")
    private Date shiftStartTimeTo;
    @ApiModelProperty("班次结束时间从")
    private Date shiftEndTimeFrom;
    @ApiModelProperty("班次结束时间至")
    private Date shiftEndTimeTo;

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

    public Date getShiftDateFrom() {
        if (shiftDateFrom != null) {
            return (Date) shiftDateFrom.clone();
        } else {
            return null;
        }
    }

    public void setShiftDateFrom(Date shiftDateFrom) {
        if (shiftDateFrom == null) {
            this.shiftDateFrom = null;
        } else {
            this.shiftDateFrom = (Date) shiftDateFrom.clone();
        }
    }

    public Date getShiftDateTo() {
        if (shiftDateTo != null) {
            return (Date) shiftDateTo.clone();
        } else {
            return null;
        }
    }

    public void setShiftDateTo(Date shiftDateTo) {
        if (shiftDateTo == null) {
            this.shiftDateTo = null;
        } else {
            this.shiftDateTo = (Date) shiftDateTo.clone();
        }
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public Date getShiftStartTimeFrom() {
        if (shiftStartTimeFrom != null) {
            return (Date) shiftStartTimeFrom.clone();
        } else {
            return null;
        }
    }

    public void setShiftStartTimeFrom(Date shiftStartTimeFrom) {
        if (shiftStartTimeFrom == null) {
            this.shiftStartTimeFrom = null;
        } else {
            this.shiftStartTimeFrom = (Date) shiftStartTimeFrom.clone();
        }
    }

    public Date getShiftStartTimeTo() {
        if (shiftStartTimeTo != null) {
            return (Date) shiftStartTimeTo.clone();
        } else {
            return null;
        }
    }

    public void setShiftStartTimeTo(Date shiftStartTimeTo) {
        if (shiftStartTimeTo == null) {
            this.shiftStartTimeTo = null;
        } else {
            this.shiftStartTimeTo = (Date) shiftStartTimeTo.clone();
        }
    }

    public Date getShiftEndTimeFrom() {
        if (shiftEndTimeFrom != null) {
            return (Date) shiftEndTimeFrom.clone();
        } else {
            return null;
        }
    }

    public void setShiftEndTimeFrom(Date shiftEndTimeFrom) {
        if (shiftEndTimeFrom == null) {
            this.shiftEndTimeFrom = null;
        } else {
            this.shiftEndTimeFrom = (Date) shiftEndTimeFrom.clone();
        }
    }

    public Date getShiftEndTimeTo() {
        if (shiftEndTimeTo != null) {
            return (Date) shiftEndTimeTo.clone();
        } else {
            return null;
        }
    }

    public void setShiftEndTimeTo(Date shiftEndTimeTo) {
        if (shiftEndTimeTo == null) {
            this.shiftEndTimeTo = null;
        } else {
            this.shiftEndTimeTo = (Date) shiftEndTimeTo.clone();
        }
    }
}
