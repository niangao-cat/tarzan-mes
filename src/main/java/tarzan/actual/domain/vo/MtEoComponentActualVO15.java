package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Leeloing
 * @date 2019/3/19 10:10
 */
public class MtEoComponentActualVO15 implements Serializable {
    private static final long serialVersionUID = 8272465003710738406L;
    private Date actualFirstTime;
    private Date actualLastTime;
    private Double periodTime;
    private String periodUom;

    public Date getActualFirstTime() {
        if (actualFirstTime == null) {
            return null;
        } else {
            return (Date) actualFirstTime.clone();
        }
    }

    public void setActualFirstTime(Date actualFirstTime) {

        if (actualFirstTime == null) {
            this.actualFirstTime = null;
        } else {
            this.actualFirstTime = (Date) actualFirstTime.clone();
        }
    }

    public Date getActualLastTime() {
        if (actualLastTime == null) {
            return null;
        } else {
            return (Date) actualLastTime.clone();
        }
    }

    public void setActualLastTime(Date actualLastTime) {

        if (actualLastTime == null) {
            this.actualLastTime = null;
        } else {
            this.actualLastTime = (Date) actualLastTime.clone();
        }
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
