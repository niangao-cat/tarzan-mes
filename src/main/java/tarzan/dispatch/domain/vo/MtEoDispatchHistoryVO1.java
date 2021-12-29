package tarzan.dispatch.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MtEoDispatchHistoryVO1 implements Serializable {
    private static final long serialVersionUID = -3358436890758911159L;

    private String workcellId; // 工作单元ID
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate; // 班次日期
    private String shiftCode; // 班次编码

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public void setShiftDate(Date shiftDate) {
        if (shiftDate != null) {
            this.shiftDate = (Date) shiftDate.clone();
        } else {
            this.shiftDate = null;
        }
    }

    public Date getShiftDate() {
        if (this.shiftDate == null) {
            return null;
        } else {
            return (Date) this.shiftDate.clone();
        }
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }
}
