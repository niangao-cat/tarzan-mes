package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by slj on 2019-02-13.
 */
public class MtWorkOrderActualVO2 implements Serializable {

    private static final long serialVersionUID = -7026069944004057627L;
    private Date actualStartTime;
    private Date actualEndTime;
    private Date planStartTime;
    private Date planEndTime;
    private Double periodTime;
    private Double PlantPeriodTime;
    private String periodUom;
    private String workOrderId;
    private String workOrderActualId;


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

    public Double getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(Double periodTime) {
        this.periodTime = periodTime;
    }

    public Double getPlantPeriodTime() {
        return PlantPeriodTime;
    }

    public void setPlantPeriodTime(Double plantPeriodTime) {
        PlantPeriodTime = plantPeriodTime;
    }

    public String getPeriodUom() {
        return periodUom;
    }

    public void setPeriodUom(String periodUom) {
        this.periodUom = periodUom;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderActualId() {
        return workOrderActualId;
    }

    public void setWorkOrderActualId(String workOrderActualId) {
        this.workOrderActualId = workOrderActualId;
    }


}
