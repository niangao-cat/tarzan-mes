package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by HP on 2019/3/15.
 */
public class MtWoComponentActualVO13 implements Serializable {

    private static final long serialVersionUID = -153858035236682059L;

    private Date actualFirstTime;
    private Date actualLastTime;
    private Double periodTime;
    private String periodUom;

    public Date getActualFirstTime() {
        return actualFirstTime == null ? null : (Date) actualFirstTime.clone();
    }

    public void setActualFirstTime(Date actualFirstTime) {
        this.actualFirstTime = actualFirstTime == null ? null : (Date) actualFirstTime.clone();
    }

    public Date getActualLastTime() {
        return actualLastTime == null ? null : (Date) actualLastTime.clone();
    }

    public void setActualLastTime(Date actualLastTime) {
        this.actualLastTime = actualLastTime == null ? null : (Date) actualLastTime.clone();
    }

    public Double getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(Double periodTime) {
        this.periodTime = periodTime;
    }

    public String getPeriodUom() {
        return periodUom;
    }

    public void setPeriodUom(String periodUom) {
        this.periodUom = periodUom;
    }
}
