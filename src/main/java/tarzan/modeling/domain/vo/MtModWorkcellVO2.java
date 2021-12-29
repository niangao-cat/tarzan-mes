package tarzan.modeling.domain.vo;

import java.io.Serializable;

import tarzan.modeling.domain.entity.MtModWorkcell;

/**
 * Created by slj on 2018-11-29.
 */
public class MtModWorkcellVO2 extends MtModWorkcell implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2487805886469781802L;
    private String workcellScheduleId;
    private String rateType;
    private Double rate;
    private Double activity;
    private Long forwardShiftsNumber;
    private Long backwarddShiftsNumber;

    public String getWorkcellScheduleId() {
        return workcellScheduleId;
    }

    public void setWorkcellScheduleId(String workcellScheduleId) {
        this.workcellScheduleId = workcellScheduleId;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getActivity() {
        return activity;
    }

    public void setActivity(Double activity) {
        this.activity = activity;
    }

    public Long getForwardShiftsNumber() {
        return forwardShiftsNumber;
    }

    public void setForwardShiftsNumber(Long forwardShiftsNumber) {
        this.forwardShiftsNumber = forwardShiftsNumber;
    }

    public Long getBackwarddShiftsNumber() {
        return backwarddShiftsNumber;
    }

    public void setBackwarddShiftsNumber(Long backwarddShiftsNumber) {
        this.backwarddShiftsNumber = backwarddShiftsNumber;
    }
}
