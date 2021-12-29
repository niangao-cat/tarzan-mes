package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by slj on 2019-02-11.
 */
public class MtWkcShiftVO6 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2876254971443865654L;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workcellId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;

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

}
