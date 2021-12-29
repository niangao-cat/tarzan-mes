package tarzan.modeling.domain.vo;

import java.io.Serializable;

public class MtModWorkcellScheduleVO implements Serializable {
    private static final long serialVersionUID = -8489314365309953655L;
    private String workcellScheduleId;
    private String workcellId;
    private String rateType;
    private String rateTypeDesc;
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

    public String getRateTypeDesc() {
        return rateTypeDesc;
    }

    public void setRateTypeDesc(String rateTypeDesc) {
        this.rateTypeDesc = rateTypeDesc;
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
