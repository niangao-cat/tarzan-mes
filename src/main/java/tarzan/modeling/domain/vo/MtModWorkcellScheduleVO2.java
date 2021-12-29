package tarzan.modeling.domain.vo;

import java.io.Serializable;

public class MtModWorkcellScheduleVO2 implements Serializable {
    private static final long serialVersionUID = 5911760137152218690L;
    private String workcellScheduleId;
    private String workcellId;
    private String rateType;
    private Double rate;
    private Double activity;

    public String getWorkcellScheduleId() {
        return workcellScheduleId;
    }

    public void setWorkcellScheduleId(String workcellScheduleId) {
        this.workcellScheduleId = workcellScheduleId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
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

}
