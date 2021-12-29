package tarzan.dispatch.api.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author : MrZ
 * @date : 2019-12-10 19:10
 **/
public class MtEoDispatchPlatformDTO16 implements Serializable {
    private static final long serialVersionUID = -8463375515304213988L;
    @ApiModelProperty(value = "主键ID", required = true)
    private String eoDispatchId;
    @ApiModelProperty(value = "调度数", required = true)
    private Double dispatchQty;
    @ApiModelProperty(value = "工作单元ID", required = true)
    private String workcellId;
    @ApiModelProperty(value = "日历日期", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    @ApiModelProperty(value = "班次", required = true)
    private String shiftCode;
    @ApiModelProperty("调度状态(DISPATCH_STATUS),PUBLISH或者UNPUBLISH")
    private String eoDispatchStatus;

    public String getEoDispatchId() {
        return eoDispatchId;
    }

    public void setEoDispatchId(String eoDispatchId) {
        this.eoDispatchId = eoDispatchId;
    }

    public Double getDispatchQty() {
        return dispatchQty;
    }

    public void setDispatchQty(Double dispatchQty) {
        this.dispatchQty = dispatchQty;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
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

    public String getEoDispatchStatus() {
        return eoDispatchStatus;
    }

    public void setEoDispatchStatus(String eoDispatchStatus) {
        this.eoDispatchStatus = eoDispatchStatus;
    }
}
