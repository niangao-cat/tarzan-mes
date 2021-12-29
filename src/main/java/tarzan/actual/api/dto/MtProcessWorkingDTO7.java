package tarzan.actual.api.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/2/13 1:56 下午
 */
public class MtProcessWorkingDTO7 implements Serializable {
    private static final long serialVersionUID = 3790360028790051982L;
    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("工作单元ID")
    private String workcellId;
    @ApiModelProperty("班次编码")
    private String shiftCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("日历日期")
    private Date shiftDate;
    @ApiModelProperty("拆分数量")
    private Double splitQty;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
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

    public Double getSplitQty() {
        return splitQty;
    }

    public void setSplitQty(Double splitQty) {
        this.splitQty = splitQty;
    }
}
