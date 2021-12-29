package tarzan.actual.domain.vo;


import java.io.Serializable;
import java.util.Date;

public class MtEoActualVO3 implements Serializable {
    private static final long serialVersionUID = 3783885739286448534L;
    private Date actualStartTime; // 实际开始时间
    private Date actualEndTime; // 实际完成时间
    private Date planStartTime; // 开始时间
    private Date planEndTime; // 结束时间
    private Double actualPeriodTime; // 实绩生产周期时长
    private Double plantPeriodTime; // 计划生产周期时长
    private String periodUom; // 生产周期时长单位

    public Date getActualStartTime() {
        if (actualStartTime == null) {
            return null;
        }
        return (Date) actualStartTime.clone();
    }

    public void setActualStartTime(Date actualStartTime) {
        if (actualStartTime == null) {
            this.actualStartTime = null;
        } else {
            this.actualStartTime = (Date) actualStartTime.clone();
        }
    }

    public Date getActualEndTime() {
        if (actualEndTime == null) {
            return null;
        }
        return (Date) actualEndTime.clone();
    }

    public void setActualEndTime(Date actualEndTime) {
        if (actualEndTime == null) {
            this.actualEndTime = null;
        } else {
            this.actualEndTime = (Date) actualEndTime.clone();
        }
    }

    public Date getPlanStartTime() {
        if (planStartTime == null) {
            return null;
        }
        return (Date) planStartTime.clone();
    }

    public void setPlanStartTime(Date planStartTime) {
        if (planStartTime == null) {
            this.planStartTime = null;
        } else {
            this.planStartTime = (Date) planStartTime.clone();
        }
    }

    public Date getPlanEndTime() {
        if (planEndTime == null) {
            return null;
        }
        return (Date) planEndTime.clone();
    }

    public void setPlanEndTime(Date planEndTime) {
        if (planEndTime == null) {
            this.planEndTime = null;
        } else {
            this.planEndTime = (Date) planEndTime.clone();
        }
    }

    public Double getActualPeriodTime() {
        return actualPeriodTime;
    }

    public void setActualPeriodTime(Double actualPeriodTime) {
        this.actualPeriodTime = actualPeriodTime;
    }

    public Double getPlantPeriodTime() {
        return plantPeriodTime;
    }

    public void setPlantPeriodTime(Double plantPeriodTime) {
        this.plantPeriodTime = plantPeriodTime;
    }

    public String getPeriodUom() {
        return periodUom;
    }

    public void setPeriodUom(String periodUom) {
        this.periodUom = periodUom;
    }
}
