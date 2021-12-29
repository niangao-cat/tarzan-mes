package tarzan.dispatch.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/12/19 6:49 下午
 */
public class MtEoDispatchActionVO23 implements Serializable {
    private static final long serialVersionUID = 309838539735253334L;
    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("步骤ID")
    private String routerStepId;
    @ApiModelProperty("未分配数量")
    private Double unassignQty;
    @ApiModelProperty("可调度数量")
    private Double dispatchableQty;

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

    public Double getUnassignQty() {
        return unassignQty;
    }

    public void setUnassignQty(Double unassignQty) {
        this.unassignQty = unassignQty;
    }

    public Double getDispatchableQty() {
        return dispatchableQty;
    }

    public void setDispatchableQty(Double dispatchableQty) {
        this.dispatchableQty = dispatchableQty;
    }
}
