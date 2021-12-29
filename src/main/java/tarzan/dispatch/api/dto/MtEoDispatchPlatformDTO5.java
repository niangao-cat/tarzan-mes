package tarzan.dispatch.api.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author : MrZ
 * @date : 2019-12-04 10:50
 **/
public class MtEoDispatchPlatformDTO5 implements Serializable {
    private static final long serialVersionUID = 8127090870526251942L;
    @ApiModelProperty(value = "班次日期", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    @ApiModelProperty(value = "班次代码", required = true)
    private String shiftCode;
    @ApiModelProperty(value = "工作单元ID", required = true)
    private String workcellId;
    @ApiModelProperty(value = "WKC产能")
    private Double capacityQty;


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


    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public Double getCapacityQty() {
        return capacityQty;
    }

    public void setCapacityQty(Double capacityQty) {
        this.capacityQty = capacityQty;
    }
}
