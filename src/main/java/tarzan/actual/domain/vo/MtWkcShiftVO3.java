package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by slj on 2019-02-11.
 */
public class MtWkcShiftVO3 implements Serializable {
    private static final long serialVersionUID = -4023966540051440578L;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workcellId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    private String shiftCode;
    private String wkcShiftId;

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public Date getShiftDate() {
        if (shiftDate == null) {
            return null;
        }
        return (Date) shiftDate.clone();
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


    public String getWkcShiftId() {
        return wkcShiftId;
    }

    public void setWkcShiftId(String wkcShiftId) {
        this.wkcShiftId = wkcShiftId;
    }


}
