package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtEoRouterVO4 implements Serializable {
    private static final long serialVersionUID = 1131996517941833705L;

    @ApiModelProperty("执行作业Id")
    private String eoId;
    @ApiModelProperty("工艺路线步骤Id")
    private String routerStepId;

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
}
