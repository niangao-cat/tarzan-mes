package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class MtEoRouterActualVO53 implements Serializable {

    private static final long serialVersionUID = 3757580322855570808L;
    @ApiModelProperty(value = "执行作业工艺路线步骤实绩唯一标识")
    private String eoStepActualId;
    @ApiModelProperty(value = "数量")
    private Double qty;

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

}


