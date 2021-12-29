package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtEoRouterActualVO40 implements Serializable {

    private static final long serialVersionUID = -3201654492968640643L;

    @ApiModelProperty(value = "执行作业工艺路线步骤实绩唯一标识")
    private String eoStepActualId;
    @ApiModelProperty(value = "数量")
    private Double qty;
    @ApiModelProperty(value = "wkcId")
    private String workcellId;

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

}


