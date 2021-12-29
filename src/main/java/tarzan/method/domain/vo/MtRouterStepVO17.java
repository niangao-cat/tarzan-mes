package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtRouterStepVO17 implements Serializable {
    private static final long serialVersionUID = 2656355499282171990L;

    @ApiModelProperty("工艺路线Id")
    private String routerId;
    @ApiModelProperty("工艺路线步骤Id")
    private String routerStepId;

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }
}
