package tarzan.dispatch.api.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author : MrZ
 * @date : 2019-12-05 11:39
 **/
public class MtEoDispatchPlatformDTO7 implements Serializable {
    private static final long serialVersionUID = 3147916586128425795L;
    @ApiModelProperty(value = "工作单元ID")
    private String workcellId;
    @ApiModelProperty(value = "班次日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    @ApiModelProperty(value = "班次代码")
    private String shiftCode;
    @ApiModelProperty(value = "调度已发布数量")
    private Double publishedQty;
    @ApiModelProperty(value = "调度未发布数量")
    private Double unPublishedQty;
    @ApiModelProperty(value = "WKC产能")
    private Double capacityQty;

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

    public Double getPublishedQty() {
        return publishedQty;
    }

    public void setPublishedQty(Double publishedQty) {
        this.publishedQty = publishedQty;
    }

    public Double getUnPublishedQty() {
        return unPublishedQty;
    }

    public void setUnPublishedQty(Double unPublishedQty) {
        this.unPublishedQty = unPublishedQty;
    }

    public Double getCapacityQty() {
        return capacityQty;
    }

    public void setCapacityQty(Double capacityQty) {
        this.capacityQty = capacityQty;
    }
}
