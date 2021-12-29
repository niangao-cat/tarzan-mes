package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author MrZ
 */
public class MtAssembleConfirmActualVO28 implements Serializable {

    private static final long serialVersionUID = -2288105258010032505L;
    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("工艺路线步骤ID")
    private String routerStepId;
    @ApiModelProperty("操作数量")
    private Double qty;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }
}


