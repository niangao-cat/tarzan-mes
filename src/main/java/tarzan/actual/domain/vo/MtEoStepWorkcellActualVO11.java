package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtEoStepWorkcellActualVO11
 * @description
 * @date 2019年12月04日 16:57
 */
public class MtEoStepWorkcellActualVO11 implements Serializable {
    private static final long serialVersionUID = -5864726458452212210L;

    @ApiModelProperty(value = "执行作业id")
    private String eoId;
    @ApiModelProperty(value = "事件时间从")
    private Date eventTimeFrom;
    @ApiModelProperty(value = "事件时间至")
    private Date eventTimeTo;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public Date getEventTimeFrom() {
        if (eventTimeFrom != null) {
            return (Date) eventTimeFrom.clone();
        } else {
            return null;
        }
    }

    public void setEventTimeFrom(Date eventTimeFrom) {
        if (eventTimeFrom == null) {
            this.eventTimeFrom = null;
        } else {
            this.eventTimeFrom = (Date) eventTimeFrom.clone();
        }
    }

    public Date getEventTimeTo() {
        if (eventTimeTo != null) {
            return (Date) eventTimeTo.clone();
        } else {
            return null;
        }
    }

    public void setEventTimeTo(Date eventTimeTo) {
        if (eventTimeTo == null) {
            this.eventTimeTo = null;
        } else {
            this.eventTimeTo = (Date) eventTimeTo.clone();
        }
    }
}
