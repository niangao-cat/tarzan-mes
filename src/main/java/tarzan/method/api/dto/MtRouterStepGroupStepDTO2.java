package tarzan.method.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtRouterStepGroupStepDTO2 implements Serializable {
    private static final long serialVersionUID = 8616162329611907797L;

    @ApiModelProperty("工艺路线步骤组分配唯一标识")
    private String routerStepGroupStepId;
    @ApiModelProperty(value = "工艺路线步骤标识")
    private String routerStepId;

    public String getRouterStepGroupStepId() {
        return routerStepGroupStepId;
    }

    public void setRouterStepGroupStepId(String routerStepGroupStepId) {
        this.routerStepGroupStepId = routerStepGroupStepId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }
}
