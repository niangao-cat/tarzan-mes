package tarzan.modeling.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModWorkcellScheduleDTO implements Serializable {

    private static final long serialVersionUID = 70245404094355275L;

    @ApiModelProperty(value = "工作单元ID，标识唯一工作单元", required = true)
    private String workcellId;
    @ApiModelProperty(value = "速率类型")
    private String rateType;
    @ApiModelProperty(value = "默认速率")
    private Double rate;
    @ApiModelProperty(value = "开动率")
    private Double activity;

    /**
     * @return 工作单元ID，标识唯一工作单元
     */
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
