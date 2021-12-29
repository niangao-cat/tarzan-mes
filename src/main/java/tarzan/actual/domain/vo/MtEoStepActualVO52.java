package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/10/26 19:11
 */
public class MtEoStepActualVO52 implements Serializable {
    private static final long serialVersionUID = -3031948981301748338L;
    @ApiModelProperty("步骤ID")
    private String routerStepId;
    @ApiModelProperty("工艺路线实绩")
    private String eoRouterActualId;
    @ApiModelProperty("步骤实绩")
    private String eoStepActualId;

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }
}
