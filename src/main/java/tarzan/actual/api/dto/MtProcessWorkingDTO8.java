package tarzan.actual.api.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/2/13 2:00 下午
 */
public class MtProcessWorkingDTO8 implements Serializable {
    private static final long serialVersionUID = -6987766838907168414L;
    @ApiModelProperty("主合并来源执行作业")
    private String primaryEoId;
    @ApiModelProperty("工作单元ID")
    private String workcellId;
    @ApiModelProperty("班次编码")
    private String shiftCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("日历日期")
    private Date shiftDate;
    @ApiModelProperty("副来源执行作业")
    private List<String> secondaryEoIds = new ArrayList<>();

    public String getPrimaryEoId() {
        return primaryEoId;
    }

    public void setPrimaryEoId(String primaryEoId) {
        this.primaryEoId = primaryEoId;
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

    public List<String> getSecondaryEoIds() {
        return secondaryEoIds;
    }

    public void setSecondaryEoIds(List<String> secondaryEoIds) {
        this.secondaryEoIds = secondaryEoIds;
    }
}
